package com.example.history.service.my_sql;

import com.example.history.dto.response.*;
import com.example.history.dto.response.PageResponse;
import com.example.history.service.http_client.MatchClient;
import com.example.history.service.http_client.ProfileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class HistoryService {
	MatchClient matchClient;
	ProfileClient profileClient;

	public PageResponse<HistoryResponse> getAllByUserId(int page, int size, String userId) {
		// Get current user profile
		ProfileResponse profileResponse = (ProfileResponse) profileClient.getById(userId).getData();
		// Get all finished match by user ID
		PageResponse<MatchResponse> matchResponsePage = (PageResponse<MatchResponse>) matchClient.getFinishedMatch(page, size, userId).getData();

		// Create a list to hold MatchResponse objects
		List<HistoryResponse> historyResponseList = new ArrayList<>();
		// Mapping match -> history
		for (MatchResponse matchResponse : matchResponsePage.getContent()) {
			// Get user's faction
			boolean isRedUser = profileResponse.getDisplayedName().equals(matchResponse.getRedUserResponse().getDisplayedName());
			// Get result
			boolean isRedWin = matchResponse.getResult().equals("Red Player Win");
			// Define match response
			HistoryResponse historyResponse = HistoryResponse.builder()
					.id(matchResponse.getId())
					.opponentUsername(isRedUser
							? matchResponse.getBlackUserResponse().getDisplayedName()
							: matchResponse.getRedUserResponse().getDisplayedName())
					.result(isRedUser
							? (isRedWin ? "Win" : "Lose")
							: (isRedWin ? "Lose" : "Win"))
					.startTime(matchResponse.getStartTime())
					.endTime(matchResponse.getEndTime())
					.build();
			// Add to response page
			historyResponseList.add(historyResponse);
		}
		// Return
		return new PageResponse<>(
				historyResponseList,
				matchResponsePage.getPage(),
				matchResponsePage.getSize(),
				matchResponsePage.getTotalElements());
	}
}
