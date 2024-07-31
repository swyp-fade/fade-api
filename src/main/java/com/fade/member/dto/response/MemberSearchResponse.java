package com.fade.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberSearchResponse(
        @Schema(description = "Matched members")
        List<MemberSearchItemResponse> matchedMembers
) {
    public record MemberSearchItemResponse(
            @Schema(description = "Member Id")
            Long memberId,
            @Schema(description = "Member username")
            String username,
            @Schema(description = "Profile image URL")
            String profileImageURL
    ) {
    }
}
