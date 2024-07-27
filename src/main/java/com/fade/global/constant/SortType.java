package com.fade.global.constant;

public enum SortType {
    ASC("ASC"),
    DESC("DESC");

    private final String sortType;

    SortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortType() {
        return sortType;
    }
}
