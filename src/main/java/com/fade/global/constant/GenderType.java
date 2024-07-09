package com.fade.global.constant;

public enum GenderType {
    MALE("MALE"),
    FEMALE("FEMALE");

    private String type;

    GenderType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
