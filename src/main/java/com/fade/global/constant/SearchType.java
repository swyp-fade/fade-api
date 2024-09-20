package com.fade.global.constant;

public enum SearchType {
    ALL("ALL"),
    VOTED("VOTED"),
    NOT_VOTED("NOT_VOTED"),
    MY_BON("MY_BON"),
    BEST("BEST");

    private final String searchType;

    SearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSearchType() {
        return searchType;
    }
}
