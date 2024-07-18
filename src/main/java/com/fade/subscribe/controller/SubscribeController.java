package com.fade.subscribe.controller;

import com.fade.global.dto.response.Response;
import com.fade.subscribe.service.SubscribeService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
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
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "구독 성공"
            )
    )
    public Response<Long> subscribe(Long fromMemberId, @PathVariable Long toMemberId) {
        return Response.success(subscribeService.subscribe(fromMemberId, toMemberId));
    }

    @DeleteMapping("/unsubscribe/{toMemberId}")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "구독 취소"
            )
    )
    public Response<Void> unSubscribe(Long fromMemberId, @PathVariable Long toMemberId) {
        subscribeService.unSubscribe(fromMemberId, toMemberId);
        return Response.success();
    }
}
