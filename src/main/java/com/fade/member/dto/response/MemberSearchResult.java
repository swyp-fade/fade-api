package com.fade.member.dto.response;


import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record MemberSearchResult(
        @Schema(description = "Matched members")
        List<MemberSearchResponse> matchedMembers
) {
}
