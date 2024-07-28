package com.fade.subscribe.controller;

import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import com.fade.subscribe.dto.request.CountSubscriberRequest;
import com.fade.subscribe.dto.request.FindSubscriberRequest;
import com.fade.subscribe.dto.response.CheckSubscribeResponse;
import com.fade.subscribe.dto.response.CountSubscriberResponse;
import com.fade.subscribe.dto.response.FindSubscriberResponse;
import com.fade.subscribe.service.SubscribeService;
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
@Tags({
        @Tag(name = "Subscribe API")
})
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/subscribe/count")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독자 수 조회"
            )
    )
    public CountSubscriberResponse countSubscriber(
            @Valid
            CountSubscriberRequest countSubscriberRequest
    ) {
        return new CountSubscriberResponse(
                subscribeService.countSubscriber(countSubscriberRequest)
        );
    }

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

    @DeleteMapping("/subscribe/{toMemberId}")
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

    @GetMapping("/subscribe/subscribers")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독자 목록 조회 성공"
            )
    )
    public FindSubscriberResponse findSubscribers(@AuthenticationPrincipal UserVo userVo, FindSubscriberRequest findSubscriberRequest) {
        return subscribeService.findSubscribers(userVo.getId(), findSubscriberRequest.nextCursor(), findSubscriberRequest.limit());
    }

    @GetMapping("/subscribe/check/{toMemberId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독 상태 체크"
            )
    )
    public CheckSubscribeResponse checkSubscribe(@AuthenticationPrincipal UserVo userVo, @PathVariable Long toMemberId) {
        return subscribeService.checkSubscribe(userVo.getId(), toMemberId);
    }
}
