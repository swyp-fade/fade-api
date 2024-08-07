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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@Tags({
        @Tag(
                name = "Subscribe API",
                description = "구독관련 API"
        )
})
public class SubscribeController {
    private final SubscribeService subscribeService;

    @GetMapping("/subscribe/count")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독자 수 조회",
                    content = @Content(schema = @Schema(implementation = CountSubscriberResponse.class))
            )
    )
    @Operation(summary = "구독자 수 조회")
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
                    description = "구독 성공",
                    content = @Content(
                            schema = @Schema(implementation = Long.class)
                    )
            )
    )
    @Parameter(
            in = ParameterIn.PATH,
            name = "toMemberId",
            schema = @Schema(type = "long"),
            required = true,
            description = "내가 구독하려는 memberId"
    )
    @Operation(
            summary = "사용자를 구독합니다."
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
    @Operation(summary = "구독 취소")
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
    @Operation(summary = "내가 구독한 구독자 리스트 조회")
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
    @Operation(summary = "상대방을 구독 했는지 체크")
    public CheckSubscribeResponse checkSubscribe(@AuthenticationPrincipal UserVo userVo, @PathVariable Long toMemberId) {
        return subscribeService.checkSubscribe(userVo.getId(), toMemberId);
    }
}
