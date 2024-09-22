package com.fade.bon.repository;

import com.fade.bon.constant.BonVoteType;
import com.fade.bon.dto.request.VoteCountRequest;

public interface CustomBonVoteRepository {
    Long countByCondition(VoteCountRequest voteCountRequest);
    Long countByCondition(VoteCountRequest voteCountRequest, BonVoteType bonVoteType);
}
