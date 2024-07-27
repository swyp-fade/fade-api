package com.fade.vote.repository;

import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.dto.response.FindVoteResponse.FindVoteItemResponse;
import com.fade.vote.entity.Vote;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface VoteRepositoryCustom {

    List<FindVoteItemResponse> findVoteUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate);
    Optional<Vote> findOldestVoteByMember(Long memberId);
    Optional<Vote> findLatestVoteByMember(Long memberId);
    FindMostVoteItemDto findMostVoteItem();
}
