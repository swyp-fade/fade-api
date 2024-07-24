package com.fade.subscribe.controller;

import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import com.fade.feed.dto.request.FindSubscribeFeedRequest;
import com.fade.feed.dto.response.FindSubscribeFeedResponse;
import com.fade.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tags({
        @Tag(name = "Subscribe API")
})
public class SubscribeController {

    private final SubscribeService subscribeService;

    @PostMapping("/subscribe/{toMemberId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독 성공"
            )
    )
    public Response<Long> subscribe(@AuthenticationPrincipal UserVo userVo, @PathVariable Long toMemberId) {
        return Response.success(subscribeService.subscribe(userVo.getId(), toMemberId));
    }

    @DeleteMapping("/unsubscribe/{toMemberId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "구독 취소"
            )
    )
    public Response<Void> unSubscribe(@AuthenticationPrincipal UserVo userVo, @PathVariable Long toMemberId) {
        subscribeService.unSubscribe(userVo.getId(), toMemberId);
        return Response.success();
    }

    @GetMapping("/subscribe/feeds")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독 피드 조회 성공"
            )
    )
    public FindSubscribeFeedResponse findSubscribeFeeds(@AuthenticationPrincipal UserVo userVo, FindSubscribeFeedRequest findSubscribeFeedRequest) {
        return subscribeService.findSubscribeFeeds(userVo.getId(), findSubscribeFeedRequest.nextCursor(), findSubscribeFeedRequest.limit());
    }
}
