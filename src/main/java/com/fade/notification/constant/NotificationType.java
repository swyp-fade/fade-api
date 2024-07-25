package com.fade.notification.constant;

import lombok.Getter;

@Getter
public enum NotificationType {
    REPORT_FEED("내 사진 신고"),
    DELETE_PHOTO("내 사진 삭제"),
    SELECTED_FA_P("페이피 선정"),
    SUSPEND_ACCOUNT("계정 정지");

    NotificationType(String notificationType) {
    }
}
