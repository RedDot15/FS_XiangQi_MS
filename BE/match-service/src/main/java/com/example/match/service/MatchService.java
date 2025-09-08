package com.example.match.service;

import com.example.match.dto.request.MatchRequest;
import com.example.match.dto.request.MoveRequest;
import com.example.match.dto.request.ResignRequest;
import com.example.match.dto.response.*;
import com.example.match.entity.my_sql.MatchEntity;
import com.example.match.entity.redis.MatchStateEntity;
import com.example.match.entity.redis.MatchStateUserEntity;
import com.example.match.exception.AppException;
import com.example.match.exception.ErrorCode;
import com.example.match.helper.MessageObject;
import com.example.match.mapper.MatchMapper;
import com.example.match.mapper.MatchStateMapper;
import com.example.match.repository.http_client.ProfileClient;
import com.example.match.repository.my_sql.MatchRepository;
import com.example.match.util.BoardUtils;
import com.example.match.util.MoveValidator;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Service
public class MatchService {
	MatchRepository matchRepository;
	SimpMessagingTemplate messagingTemplate;
	RedisMatchService redisMatchService;
	MatchMapper matchMapper;
    ProfileClient profileClient;

	private static final long USER_TOTAL_TIME_LEFT = 60_000 * 15;
	private static final long USER_TURN_TIME_EXPIRATION = 60_000 * 1;
	private static final long USER_TOTAL_TIME_EXPIRATION = 60_000 * 15;
	private final MatchStateMapper matchStateMapper;

	public PageResponse<MatchResponse> getAllFinished(int page, int size, String userId) {
		// Define pageable
		Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "startTime"));
		// Find all finished match
		Page<MatchEntity> matchEntityPage = matchRepository.findAllFinished(pageable, userId);
		// Mapping to match response list
		List<MatchResponse> matchResponseList = matchEntityPage.getContent()
				.stream().map(matchMapper::toResponse)
				.collect(Collectors.toList());

		return new PageResponse<>(matchResponseList, matchEntityPage.getPageable(), matchEntityPage.getTotalElements());
	}

	public String createMatch(MatchRequest request) {
		MatchEntity matchEntity = new MatchEntity();

		// Find the two users
		ProfileResponse user1Profile = (ProfileResponse) profileClient.getById(request.getUser1Id()).getData();
		ProfileResponse user2Profile = (ProfileResponse) profileClient.getById(request.getUser2Id()).getData();

		// Randomly assign colors
		boolean firstIsRed = Math.random() < 0.5;
		matchEntity.setRedUserId(firstIsRed ? request.getUser1Id() : request.getUser2Id());
		matchEntity.setBlackUserId(firstIsRed ? request.getUser2Id(): request.getUser1Id());

		// Save match
		matchRepository.save(matchEntity);

		// Initial match state
		redisMatchService.saveMatchState(
				matchEntity.getId().toString(),
				MatchStateEntity.builder()
						.boardState(BoardUtils.getInitialBoardState())
						.redUser(MatchStateUserEntity.builder()
								.id(firstIsRed ? request.getUser1Id() : request.getUser2Id())
								.name(firstIsRed ? user1Profile.getDisplayedName() : user2Profile.getDisplayedName())
								.rating(firstIsRed ? user1Profile.getRating() : user2Profile.getRating())
								.totalTimeLeft(USER_TOTAL_TIME_LEFT)
								.build())
						.blackUser(MatchStateUserEntity.builder()
								.id(firstIsRed ? request.getUser2Id() : request.getUser1Id())
								.name(firstIsRed ? user2Profile.getDisplayedName() : user1Profile.getDisplayedName())
								.rating(firstIsRed ? user2Profile.getRating() : user1Profile.getRating())
								.totalTimeLeft(USER_TOTAL_TIME_LEFT)
								.build())
						.turn(firstIsRed ? request.getUser1Id() : request.getUser2Id())
						.lastMoveTime(Instant.now())
						.mode(request.getIsRank() ? "RANK" : "NORMAL")
						.build());
		// Initial match expiration
		redisMatchService.saveMatchExpiration(matchEntity.getId().toString(),
				Math.min(USER_TURN_TIME_EXPIRATION, USER_TOTAL_TIME_EXPIRATION));

		return matchEntity.getId().toString();
	}

	public MatchStateResponse getMatchStateById(String matchId) {
		// Get match state
		MatchStateEntity entity = redisMatchService.getMatchState(matchId);
		// Mapping & Return match state
		return matchStateMapper.toResponse(entity);
	}

	public void move(MoveRequest moveRequest) {
		// Get match state
		MatchStateEntity msEntity = redisMatchService.getMatchState(moveRequest.getMatchId());

		// Check if the piece belongs to the current user
		if (!isCorrectTurn(msEntity, moveRequest))
			throw new AppException(ErrorCode.UNAUTHORIZED);
		// Move validate
		if (!MoveValidator.isValidMove(msEntity.getBoardState(), moveRequest))
			throw new AppException(ErrorCode.INVALID_MOVE);

		// Apply move & update Redis
		applyMove(moveRequest.getMatchId(), msEntity, moveRequest);

		// Notify users via WebSocket
		messagingTemplate.convertAndSend("/topic/match/" + moveRequest.getMatchId(),
				new MessageObject("Piece moved.", new MoveResponse(moveRequest.getFrom(), moveRequest.getTo())));

		// Check if opponent has legal moves
		boolean opponentIsRed = msEntity.getRedUser().getId().equals(msEntity.getTurn());
		if (!MoveValidator.hasLegalMoves(msEntity.getBoardState(), opponentIsRed))
			endMatch(moveRequest.getMatchId(), opponentIsRed
					? msEntity.getRedUser().getId()
					: msEntity.getBlackUser().getId());
	}

	private static boolean isCorrectTurn(MatchStateEntity msEntity, MoveRequest request) {
		// Get match state
		String redUserId = msEntity.getRedUser().getId();
		String blackUserId = msEntity.getBlackUser().getId();
		String turn = msEntity.getTurn();
		// Get moved piece
		String movedPiece = msEntity.getBoardState()[request.getFrom().getRow()][request.getFrom().getCol()];

		return (Character.isUpperCase(movedPiece.charAt(0)) && request.getMoverId().equals(redUserId) && request.getMoverId().equals(turn)) ||
				(Character.isLowerCase(movedPiece.charAt(0)) && request.getMoverId().equals(blackUserId) && request.getMoverId().equals(turn));
	}

	public void resign(ResignRequest resignRequest) {
		// Get user ID
		MatchStateEntity msEntity = redisMatchService.getMatchState(resignRequest.getMatchId());
		String redUserId = msEntity.getRedUser().getId();
		String blackUserId = msEntity.getBlackUser().getId();

		if (!resignRequest.getSurrenderId().equals(redUserId) && !resignRequest.getSurrenderId().equals(blackUserId))
			throw new AppException(ErrorCode.UNAUTHORIZED);

		endMatch(resignRequest.getMatchId(), resignRequest.getSurrenderId().equals(redUserId) ? redUserId : blackUserId);
	}

	private void applyMove(String matchId, MatchStateEntity msEntity, MoveRequest moveRequest) {
		// Get match state
		String[][] boardState = msEntity.getBoardState();
		MatchStateUserEntity redUser = msEntity.getRedUser();
		MatchStateUserEntity blackUser = msEntity.getBlackUser();
		Instant lastMoveTime = msEntity.getLastMoveTime();
		// Get move request detail
		int fromRow = moveRequest.getFrom().getRow();
		int fromCol = moveRequest.getFrom().getCol();
		int toRow = moveRequest.getTo().getRow();
		int toCol = moveRequest.getTo().getCol();
		// Get user's faction
		boolean isRedUser = msEntity.getTurn().equals(redUser.getId());

		// Apply the move (update board state)
		boardState[toRow][toCol] = boardState[fromRow][fromCol]; // Move piece
		boardState[fromRow][fromCol] = ""; // Clear old position
		// Update turns
		String nextTurn = isRedUser ? blackUser.getId() : redUser.getId();
		msEntity.setTurn(nextTurn);
		// Get current user's total time-left
		Long currentUserTimeLeft = isRedUser ? redUser.getTotalTimeLeft() : blackUser.getTotalTimeLeft();
		// Calculate user's total time-left
		Long updatedUserTimeLeft = currentUserTimeLeft -
				(Instant.now().toEpochMilli()-lastMoveTime.toEpochMilli());
		// Update user's time-left
		if (isRedUser)
			redUser.setTotalTimeLeft(updatedUserTimeLeft);
		else
			blackUser.setTotalTimeLeft(updatedUserTimeLeft);
		// Update Last Move Time
		msEntity.setLastMoveTime(Instant.now());

		// Update match state
		redisMatchService.saveMatchState(matchId, msEntity);

		// Get opponent user's total time-left
		Long opponentUserTimeLeft = isRedUser
				? blackUser.getTotalTimeLeft()
				: redUser.getTotalTimeLeft();

		// Set new match expiration
		redisMatchService.saveMatchExpiration(matchId, Math.min(USER_TURN_TIME_EXPIRATION, opponentUserTimeLeft));
	}

	public void handleMatchExpiration(String matchId) {
		// Get match state
		MatchStateEntity entity = redisMatchService.getMatchState(matchId);
		// End match
		endMatch(matchId, entity.getTurn());
	}

	private void endMatch(String matchId, String loserId) {
		// Get match state
		MatchStateEntity msEntity = redisMatchService.getMatchState(matchId);
		// Get User ID
		String redUserId = msEntity.getRedUser().getId();
		String blackUserId = msEntity.getBlackUser().getId();

		// Get user's faction
		boolean	isRedLose = loserId.equals(redUserId);

		// Update match info
		MatchEntity matchEntity = matchRepository.findById(UUID.fromString(matchId))
				.orElseThrow(() -> new AppException(ErrorCode.MATCH_NOT_FOUND));
		matchEntity.setResult(isRedLose ? "Black User Win" : "Red User Win"); // Opponent wins
		matchEntity.setEndTime(Instant.now());
		matchRepository.save(matchEntity);

		// Get mode
		boolean isRank = msEntity.getMode().equals("RANK");

		if (isRank) {
			// Update red's elo
            profileClient.updateRating(redUserId, isRedLose ? -10 : +10);
			// Update black's elo
            profileClient.updateRating(redUserId, isRedLose ? +10 : -10);
		}

		// Delete match state
		redisMatchService.deleteMatchState(matchEntity.getId().toString());
		redisMatchService.deleteMatchExpiration(matchEntity.getId().toString());

		messagingTemplate.convertAndSend("/topic/match/" + matchId,
				new MessageObject(
						"Match finished.",
						new MatchResultResponse(
								isRedLose ? "black" : "red",
								isRank ? +10 : 0,
								isRank ? -10 : 0)));
	}
}
