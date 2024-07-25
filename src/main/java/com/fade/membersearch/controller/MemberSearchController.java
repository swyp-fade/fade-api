package com.fade.membersearch.controller;

import com.fade.membersearch.dto.response.MemberSearchResponse;
import com.fade.membersearch.service.MemberSearchService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberSearchController {
    private final MemberSearchService memberSearchService;

    @GetMapping("/search")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = MemberSearchResponse.class))
            )
    )
    public List<MemberSearchResponse> searchMembers(
            @RequestParam String query
    ) {
        return this.memberSearchService.searchMembers(query);
    }
}
