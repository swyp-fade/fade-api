package com.fade.vote.controller;

import com.fade.vote.dto.request.CreateVoteRequest;
import com.fade.vote.dto.response.CreateVoteResponse;
import com.fade.vote.dto.response.FindVoteResponse;
import com.fade.vote.service.VoteService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("vote")
@Tags({
        @Tag(name = "Vote API")
})
public class VoteController {

    private final VoteService voteService;

    @PostMapping("")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateVoteResponse.class))
            )
    )
    public CreateVoteResponse vote(@Valid @RequestBody CreateVoteRequest voteRequest) {
        return new CreateVoteResponse(voteService.createVote(1L, voteRequest.voteItems()));
    }

    @GetMapping("")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindVoteResponse.class))
            )
    )
    public FindVoteResponse getVoteResult(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate nextCursor, @RequestParam int limit, @RequestParam String scrollType) {
        return voteService.findVotes(1L, nextCursor, limit, scrollType);
    }
}
