package com.fade.attachment.constant;

public enum AttachmentLinkType {
    PROFILE("PROFILE"),
    IMAGE("IMAGE");

    private String type;

    AttachmentLinkType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
