package com.fade.feed.controller;

import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.response.CreateFeedResponse;
import com.fade.feed.service.FeedService;
import com.fade.member.constant.MemberRole;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CreateFeedResponse createFeed(
            @RequestBody CreateFeedRequest createFeedRequest
    ) {
        return new CreateFeedResponse(
                feedService.createFeed(1L, createFeedRequest)
        );
    }
}
