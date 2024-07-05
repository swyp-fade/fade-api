package com.fade.vote.dto.request;

import com.fade.vote.constant.VoteType;

import java.time.LocalDateTime;

public record VoteItemRequest(
        long postId,

        VoteType voteType,

        LocalDateTime votedAt) {
}
