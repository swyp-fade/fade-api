package com.fade.bon.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum BonVoteType {
    YES,
    NO,
    NOT;

    @JsonCreator
    public static BonVoteType parsing(String inputValue) {
        return Stream.of(BonVoteType.values())
                .filter(bonVoteType -> bonVoteType.toString().equals(inputValue.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
