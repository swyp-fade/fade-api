package com.fade.vote.repository;

import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.entity.Vote;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepositoryCustom {

    List<Vote> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    Vote findOldestVoteByMember(Long memberId);
    Vote findLatestVoteByMember(Long memberId);
    Vote findNextUpCursor(LocalDateTime lastUpCursor);
    Vote findNextDownCursor(LocalDateTime lastDownCursor);
    FindMostVoteItemDto findMostVoteItem();
}
