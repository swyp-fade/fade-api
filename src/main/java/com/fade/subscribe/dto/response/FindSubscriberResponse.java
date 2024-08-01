package com.fade.subscribe.dto.response;

import java.util.List;

public record FindSubscriberResponse(
        List<FindSubscriberItemResponse> subscribers,
        Long nextCursor,
        Long totalSubscribers
) {
    public record FindSubscriberItemResponse(
        Long id,
        String username,
        String profileImageURL
    ) {
    }
}
