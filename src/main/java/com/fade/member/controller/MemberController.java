package com.fade.member.controller;

import com.fade.member.constant.MemberRole;
import com.fade.member.dto.request.ModifyMemberRequest;
import com.fade.member.dto.response.FindMemberDetailResponse;
import com.fade.member.service.MemberService;
import com.fade.member.vo.UserVo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/me")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindMemberDetailResponse.class))
            )
    )
    public FindMemberDetailResponse findMyMemberDetail(
            @AuthenticationPrincipal UserVo userVo
    ) {
        return this.memberService.findMemberDetail(userVo.getId());
    }

    @PutMapping("/me")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204"
            )
    )
    public void modifyUser(
            @RequestBody ModifyMemberRequest modifyMemberRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        this.memberService.modifyMember(userVo.getId(), modifyMemberRequest);
    }
}
