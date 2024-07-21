package com.fade.vote.repository;

import com.fade.vote.dto.response.VoteResultResponse.VoteResultItemResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface VoteRepositoryCustom {

    List<VoteResultItemResponse> getVoteResultUsingNoOffset(Long memberId, LocalDateTime startDate, LocalDateTime endDate);

}
