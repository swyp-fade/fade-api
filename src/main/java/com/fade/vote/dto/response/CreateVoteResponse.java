package com.fade.vote.dto.response;

import java.util.List;

public record CreateVoteResponse(
        List<CreateVoteItemResponse> voteItemsId
) {
    public record CreateVoteItemResponse(
            Long voteItemId) {
    }
}

