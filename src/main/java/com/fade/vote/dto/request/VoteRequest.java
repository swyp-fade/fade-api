package com.fade.vote.dto.request;

import java.util.List;

public record VoteRequest(
        List<VoteItemRequest> voteItems) {
}
