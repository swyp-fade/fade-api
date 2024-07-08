package com.fade.attachment.constant;

public enum AttachmentLinkableType {
    FEED("FEED"),
    USER("USER");

    private String type;

    AttachmentLinkableType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
