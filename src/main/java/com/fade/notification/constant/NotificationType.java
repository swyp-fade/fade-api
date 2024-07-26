package com.fade.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationType {
    FEED_REPORTED("사진 신고"),
    FEED_DELETED("사진 삭제"),
    FAP_SELECTED("페이피 선정"),
    FAP_DELETED("페이피 사진 삭제");

    NotificationType(String notificationType) {
    }
}
