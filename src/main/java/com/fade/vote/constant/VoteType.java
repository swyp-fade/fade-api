package com.fade.vote.constant;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.stream.Stream;

public enum VoteType {
    FADE_IN,
    FADE_OUT;

    @JsonCreator
    public static VoteType parsing(String inputValue) {
        return Stream.of(VoteType.values())
                .filter(voteType -> voteType.toString().equals(inputValue.toUpperCase()))
                .findAny()
                .orElse(null);
    }
}
