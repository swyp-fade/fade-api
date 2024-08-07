package com.fade.feed.controller;

import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.request.ModifyFeedRequest;
import com.fade.feed.dto.response.CreateFeedResponse;
import com.fade.feed.dto.response.FindFeedDetailResponse;
import com.fade.feed.dto.response.FindFeedResponse;
import com.fade.feed.service.FeedService;
import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
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

@RestController()
@RequestMapping("feeds")
@Tags({
        @Tag(name = "Feed API")
})
@RequiredArgsConstructor
public class FeedController {
    private final FeedService feedService;

    @PostMapping("")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(implementation = CreateFeedResponse.class))
            )
    )
    @Operation(summary = "새로운 피드를 생성합니다.")
    public CreateFeedResponse createFeed(
            @RequestBody @Valid CreateFeedRequest createFeedRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        return new CreateFeedResponse(
                feedService.createFeed(userVo.getId(), createFeedRequest)
        );
    }

    @GetMapping("")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = FindFeedResponse.class))
    )
    @Operation(summary = "피드 리스트를 불러옵니다.")
    public FindFeedResponse findFeeds(
            @Valid
            FindFeedRequest findFeedRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        return feedService.findFeeds(
                findFeedRequest,
                userVo.getId()
        );
    }

    @GetMapping("{feedId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = FindFeedDetailResponse.class))
    )
    @Operation(summary = "피드의 상세 정보를 불러옵니다.")
    public FindFeedDetailResponse findFeedDetail(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable(name = "feedId") Long feedId
    ) {
        return feedService.findFeed(
                feedId,
                userVo.getId()
        );
    }

    @PatchMapping("/{feedId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "피드 수정"
            )
    )
    @Operation(summary = "피드를 수정합니다.")
    public void modifyFeed(@RequestBody ModifyFeedRequest modifyFeedRequest,
                           @AuthenticationPrincipal UserVo userVo,
                           @PathVariable Long feedId) {
        this.feedService.modifyFeed(modifyFeedRequest, userVo.getId(), feedId);
    }

    @DeleteMapping("/{feedId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "피드 삭제"
            )
    )
    @Operation(summary = "피드를 삭제합니다.")
    public Response<Void> deleteFeed(@AuthenticationPrincipal UserVo userVo, @PathVariable Long feedId) {
        this.feedService.deleteFeed(userVo.getId(), feedId);
        return Response.success();
    }
}
