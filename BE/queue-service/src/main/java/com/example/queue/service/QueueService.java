package com.example.queue.service;

import com.example.queue.dto.request.ContractPlayerRequest;
import com.example.queue.dto.request.MatchContractRequest;
import com.example.queue.dto.request.QueueJoinRequest;
import com.example.queue.dto.request.QueueLeaveRequest;
import com.example.queue.helper.MessageObject;
import com.example.queue.repository.http_client.MatchContractClient;
import com.example.queue.repository.http_client.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class QueueService {
    SimpMessagingTemplate messagingTemplate;
    RedisQueueService redisQueueService;
    ProfileClient profileClient;
    MatchContractClient matchContractClient;

    public void joinQueue(QueueJoinRequest joinRequest) {
        // Get current player's rank
        Integer myRating = (Integer) profileClient.getRatingById(joinRequest.getJoinerId()).getData();

        // Wait to acquire lock
        redisQueueService.acquireQueueLock();

        String opponentId = null;
        try {
            // Browse for opponent with equivalent rank
            Long queueSize = redisQueueService.getQueueSize();
            for (int i = 0; i < queueSize; i++) {
                String potentialOpponentId = redisQueueService.getPlayerIdByIndex(i);
                if (!potentialOpponentId.equals(joinRequest.getJoinerId())) {
                    Integer opponentRank = (Integer) profileClient.getRatingById(potentialOpponentId).getData();
                    // Match if opponent's rank is equivalent
                    if (Math.abs(myRating - opponentRank) <= 100) {
                        opponentId = potentialOpponentId;
                        // Remove opponent's ID from queue
                        redisQueueService.deletePlayerId(opponentId);
                        break;
                    }
                }
            }
        } finally {
            // Always release the lock
            redisQueueService.releaseQueueLock();
        }

        if (opponentId != null) {
            // Save new match-contract
            MatchContractRequest matchContractRequest = MatchContractRequest.builder()
                    .player1(new ContractPlayerRequest(joinRequest.getJoinerId()))
                    .player2(new ContractPlayerRequest(opponentId))
                    .build();
            String matchContractId = (String) matchContractClient.create(matchContractRequest).getData();

            // Notify players via WebSocket
            messagingTemplate.convertAndSend("/topic/queue/player/" + opponentId,
                    new MessageObject("Match found.", matchContractId));
            messagingTemplate.convertAndSend("/topic/queue/player/" + joinRequest.getJoinerId(),
                    new MessageObject("Match found.", matchContractId));
        } else {
            // Check if playerId already exists in the queue
            List<String> queue = redisQueueService.getAll();
            if (!queue.contains(joinRequest.getJoinerId())) {
                // No opponent yet, add this player to the queue
                redisQueueService.rightPush(joinRequest.getJoinerId());
            }
            // Notify join queue success
            messagingTemplate.convertAndSend("/topic/queue/player/" + joinRequest.getJoinerId(),
                    new MessageObject("Join queue success.", null));
        }
    }

    public void leaveQueue(QueueLeaveRequest request){
        // Wait to acquire lock
        redisQueueService.acquireQueueLock();

        try {
            // Remove my Id from queue
            redisQueueService.deletePlayerId(request.getLeaverId());
        } finally {
            // Release lock
            redisQueueService.releaseQueueLock();
        }

        // Notify leave queue success
        messagingTemplate.convertAndSend("/topic/queue/player/" + request.getLeaverId(),
                new MessageObject("Leave queue success.", null));
    }


}
