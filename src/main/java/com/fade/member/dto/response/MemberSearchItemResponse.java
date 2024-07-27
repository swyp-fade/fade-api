package com.fade.member.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MemberSearchItemResponse(
        @Schema(description = "Member username")
        String username,
        @Schema(description = "Profile image URL")
        String profileImageUrl
) {
}
