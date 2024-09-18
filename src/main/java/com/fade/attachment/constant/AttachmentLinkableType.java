package com.fade.attachment.constant;

public enum AttachmentLinkableType {
    FEED("FEED"),
    USER("USER"),
    BON("BON");

    private String type;

    AttachmentLinkableType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
