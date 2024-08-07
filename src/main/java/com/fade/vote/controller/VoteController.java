package com.fade.vote.controller;

import com.fade.feed.dto.response.ExtractRandomFeedResponse;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import com.fade.vote.dto.request.CreateVoteRequest;
import com.fade.vote.dto.request.FindVoteRequest;
import com.fade.vote.dto.response.CreateVoteResponse;
import com.fade.vote.dto.response.FindVoteResponse;
import com.fade.vote.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("vote")
@Tags({
        @Tag(name = "Vote API")
})
public class VoteController {

    private final VoteService voteService;

    @GetMapping("/candidates")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ExtractRandomFeedResponse.class))
            )
    )
    @Operation(
            summary = "투표 할 피드 목록을 조회합니다.",
            description = "이미 투표한 피드와 본인의 피드는 제외 후 조회합니다."
    )
    public ExtractRandomFeedResponse extractRandomFeeds(@AuthenticationPrincipal UserVo userVo) {
        return voteService.extractRandomFeeds(userVo.getId());
    }

    @PostMapping("/candidates")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateVoteResponse.class))
            )
    )
    @Operation(
            summary = "투표를 완료하는 API 입니다.",
            description = "1 ~ 10 개의 투표 기록을 DB에 저장합니다."
    )
    public CreateVoteResponse vote(@AuthenticationPrincipal UserVo userVo, @Valid @RequestBody CreateVoteRequest voteRequest) {
        return new CreateVoteResponse(voteService.createVote(userVo.getId(), voteRequest.voteItems()));
    }

    @GetMapping("/history")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindVoteResponse.class))
            )
    )
    @Operation(
            summary = "본인의 투표 기록을 조회합니다.",
            description = "일별 투표 기록 조회"
    )
    public FindVoteResponse findVotes(@AuthenticationPrincipal UserVo userVo, FindVoteRequest findVoteRequest) {
        return voteService.findVotes(userVo.getId(), findVoteRequest);
    }
}
