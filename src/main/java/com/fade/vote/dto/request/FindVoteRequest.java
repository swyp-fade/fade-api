package com.fade.vote.dto.request;

import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

public record FindVoteRequest(
        @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nextCursor,
        Integer limit,
        String scrollType
) {
    public FindVoteRequest {
        if (nextCursor == null) {
            nextCursor = LocalDate.now();
        }
        if (limit == null) {
            limit = 10;
        }

        if (scrollType == null) {
            scrollType = "0";
        }
    }
}
