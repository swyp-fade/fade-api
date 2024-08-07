package com.fade.vote.repository;

import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.dto.request.CountVoteRequest;
import com.fade.vote.entity.Vote;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepositoryCustom {

    List<Vote> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    Vote findNextUpCursor(LocalDateTime lastUpCursor, Long memberId);
    Vote findNextDownCursor(LocalDateTime lastDownCursor, Long memberId);
    FindMostVoteItemDto findMostVoteItem();
    Long countByCondition(CountVoteRequest countVoteRequest);
}
