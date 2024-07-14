package com.fade.bookmark.dto;

public record BookmarkResponseDto(
        Long id,
        String title,
        String url,
        String description,
        Long memberId,
        Long feedId
) {
    // 추가적으로 memberId와 feedId의 getter 메서드 정의
    public Long getMemberId() {
        return memberId;
    }

    public Long getFeedId() {
        return feedId;
    }
}