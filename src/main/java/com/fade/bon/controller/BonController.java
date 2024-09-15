package com.fade.bon.controller;

import com.fade.bon.dto.request.CreateBonCommentReq;
import com.fade.bon.dto.request.CreateBonReqDto;
import com.fade.bon.dto.response.CreateBonCommentRes;
import com.fade.bon.dto.response.CreateBonResDto;
import com.fade.bon.service.BonService;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("bon")
@RestController()
@RequiredArgsConstructor
public class BonController {
    private final BonService bonService;

    @PostMapping("")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateBonResDto.class))
            )
    )
    public CreateBonResDto createBon(
            @Valid @RequestBody CreateBonReqDto createBonReqDto,
            @AuthenticationPrincipal UserVo userVo
    ) {
        final var bonId = this.bonService.createBon(userVo.getId(), createBonReqDto);

        return new CreateBonResDto(bonId);
    }

    @PostMapping("{bonId}/comment")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateBonCommentRes.class))
            )
    )
    public CreateBonCommentRes createBonComment(
            @Valid @RequestBody CreateBonCommentReq createBonCommentReq,
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("bonId") Long bonId
    ) {
        final var bonCommentId = this.bonService.createBonComment(userVo.getId(), bonId, createBonCommentReq);

        return new CreateBonCommentRes(bonCommentId);
    }

    @DeleteMapping("{bonId}/comment/{commentId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateBonCommentRes.class))
            )
    )
    public CreateBonCommentRes deleteBonComment(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("commentId") Long commentId
    ) {
        this.bonService.deleteBonComment(userVo.getId(), commentId);

        return new CreateBonCommentRes(commentId);
    }
}
