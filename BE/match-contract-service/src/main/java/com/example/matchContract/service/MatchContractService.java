package com.example.matchContract.service;

import com.example.matchContract.dto.request.ContractAcceptRequest;
import com.example.matchContract.dto.request.MatchContractRequest;
import com.example.matchContract.dto.request.MatchRequest;
import com.example.matchContract.entity.redis.MatchContractEntity;
import com.example.matchContract.exception.AppException;
import com.example.matchContract.exception.ErrorCode;
import com.example.matchContract.helper.MessageObject;
import com.example.matchContract.mapper.MatchContractMapper;
import com.example.matchContract.repository.http_client.MatchClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class MatchContractService {
    MatchContractMapper matchContractMapper;
    RedisMatchContractService redisMatchContractService;
    SimpMessagingTemplate messagingTemplate;
    MatchClient matchClient;

    private static final long MATCH_CONTRACT_EXPIRATION = 5_000 * 1;

    public String create(MatchContractRequest request) {
        // Mapping
        MatchContractEntity entity = matchContractMapper.toEntity(request);
        // Set default accept status: false
        entity.getUser1().setAcceptStatus(false);
        entity.getUser2().setAcceptStatus(false);
        // Generate match contract ID
        String matchContractId = UUID.randomUUID().toString();
        // Initial match contract
        redisMatchContractService.saveMatchContract(matchContractId, entity);
        redisMatchContractService.saveMatchContractExpiration(matchContractId, MATCH_CONTRACT_EXPIRATION);
        // Return match contract ID
        return matchContractId;
    }

    public void accept(ContractAcceptRequest request){
        // Get match contract
        MatchContractEntity mcEntity1 = redisMatchContractService.getMatchContract(request.getMatchContractId());
        // Match contract not found exception
        if (mcEntity1 == null)
            throw new AppException(ErrorCode.MATCH_CONTRACT_NOT_FOUND);
        if (!request.getAcceptorId().equals(mcEntity1.getUser1().getId()) && !request.getAcceptorId().equals(mcEntity1.getUser2().getId()))
            throw new AppException(ErrorCode.UNAUTHORIZED);

        // Get side
        boolean isUser1 = request.getAcceptorId().equals(mcEntity1.getUser1().getId());

        // Update the accept status: true
        if (isUser1)
            mcEntity1.getUser1().setAcceptStatus(true);
        else
            mcEntity1.getUser2().setAcceptStatus(true);
        redisMatchContractService.saveMatchContract(request.getMatchContractId(), mcEntity1);

        // Acquire lock
        redisMatchContractService.acquireMatchContractLock(request.getMatchContractId());

        // Check to start match
        try {
            // Get match contract
            MatchContractEntity mcEntity2 = redisMatchContractService.getMatchContract(request.getMatchContractId());

            // Start the match if the opponent is ready
            if (mcEntity2 != null) {
                // Get opponent's accept-status
                boolean opponentAcceptStatus = isUser1
                        ? mcEntity2.getUser2().getAcceptStatus()
                        : mcEntity2.getUser1().getAcceptStatus();
                // Opponent is ready
                if (opponentAcceptStatus) {
                    // Delete match contract
                    redisMatchContractService.deleteMatchContract(request.getMatchContractId());
                    redisMatchContractService.deleteMatchContractExpiration(request.getMatchContractId());

                    // Create match
                    String matchId = (String) matchClient.createMatch(MatchRequest.builder()
                            .user1Id(mcEntity1.getUser1().getId())
                            .user2Id(mcEntity2.getUser2().getId())
                            .isRank(true)
                            .build()).getData();

                    // Notify both player: The match is start
                    messagingTemplate.convertAndSend("/topic/match-contract/" + request.getMatchContractId(),
                            new MessageObject("The match is created.", matchId));
                }
            }
        } finally {
            redisMatchContractService.releaseMatchContractLock(request.getMatchContractId());
        }
    }

    public void handleMatchContractExpiration(String matchContractId) {
        // Delete match contract
        redisMatchContractService.deleteMatchContract(matchContractId);
        // Notify both players match contract timeout
        messagingTemplate.convertAndSend("/topic/match-contract/" + matchContractId,
                new MessageObject("Match contract timeout.", null));
    }
}
