package com.fade.vote.controller;

import com.fade.global.dto.response.Response;
import com.fade.vote.dto.request.CreateVoteRequest;
import com.fade.vote.dto.response.CreateVoteResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("vote")
@Tags({
        @Tag(name = "Vote API")
})
public class VoteController {

    @PostMapping("")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateVoteResponse.class))
            )
    )
    public CreateVoteResponse vote(@RequestBody CreateVoteRequest voteRequest) {
        return null;
    }
}
