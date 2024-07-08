package com.fade.attachment.constant;

public enum AttachmentType {
    IMAGE("IMAGE");

    private String type;

    AttachmentType(String type){
        this.type = type;
    }

    public String getType(){
        return type;
    }
}
