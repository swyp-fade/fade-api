package com.fade.member.controller;

import com.fade.auth.service.AuthService;
import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.dto.request.ModifyMemberRequest;
import com.fade.member.dto.response.FindMemberDetailResponse;
import com.fade.member.dto.response.FindMyMemberDetailResponse;
import com.fade.member.dto.response.MemberSearchItemResponse;
import com.fade.member.dto.response.MemberSearchResponse;
import com.fade.member.service.MemberService;
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

@RestController
@RequiredArgsConstructor
@Tags({
        @Tag(name = "Member API")
})
@RequestMapping("members")
public class MemberController {
    private final MemberService memberService;
    private final AuthService authService;

    @GetMapping("/{memberId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindMemberDetailResponse.class))
            )
    )
    public FindMemberDetailResponse findMemberDetail(
            @PathVariable(name = "memberId") Long memberId,
            @AuthenticationPrincipal UserVo userVo
    ) {
        return this.memberService.findMemberDetail(memberId, userVo.getId());
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindMyMemberDetailResponse.class))
            )
    )
    public FindMyMemberDetailResponse findMyMemberDetail(
            @AuthenticationPrincipal UserVo userVo
    ) {
        return this.memberService.findMemberDetail(userVo.getId(), userVo.getId());
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
            @RequestBody @Valid ModifyMemberRequest modifyMemberRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        this.memberService.modifyMember(userVo.getId(), modifyMemberRequest);
    }

    @GetMapping("/search")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = MemberSearchItemResponse.class))
            )
    )
    public MemberSearchResponse searchMembers(
            @RequestParam String query
    ) {
        return this.memberService.searchMembers(query);
    }

    @DeleteMapping("/me")
    @Operation(
            summary = "계정 탈퇴"
    )
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204"
            )
    )
    public Response<Void> deleteMember(@AuthenticationPrincipal UserVo userVo) {
        this.memberService.deleteMember(userVo.getId());

        return Response.success();
    }

    @PostMapping("/signin")
    public String signin(Long memberId) {
        return authService.generateAccessToken(memberId);
    }
}
