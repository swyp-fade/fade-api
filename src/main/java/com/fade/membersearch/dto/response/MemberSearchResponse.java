package com.fade.membersearch.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSearchResponse(
        @Schema(description = "Member username")
        String username
) {
}