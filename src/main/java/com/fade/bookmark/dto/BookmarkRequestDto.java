package com.fade.bookmark.dto;

public record BookmarkRequestDto(String title, String url, String description, Long memberId, Long feedId) {
}
