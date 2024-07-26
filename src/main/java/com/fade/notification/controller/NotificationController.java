package com.fade.notification.controller;

import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import com.fade.notification.dto.request.FindNotificationRequest;
import com.fade.notification.dto.response.FindNotificationResponse;
import com.fade.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/history")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindNotificationResponse.class)),
                    description = "알림 내역 조회 성공"
            )
    )
    public FindNotificationResponse findNotifications(@AuthenticationPrincipal UserVo userVo, FindNotificationRequest findNotificationRequest) {
        return notificationService.findNotifications(userVo.getId(), findNotificationRequest.nextCursor(), findNotificationRequest.limit());
    }

    @PostMapping("/read")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "알림 읽음 처리 성공"
            )
    )
    public Response<String> readNotifications(@AuthenticationPrincipal UserVo userVo) {
        notificationService.readNotifications(userVo.getId());
        return Response.success("알림 읽음 처리 성공");
    }
}
