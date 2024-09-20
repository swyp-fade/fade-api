package com.fade.bon.controller;

import com.fade.bon.dto.request.CreateBonCommentReq;
import com.fade.bon.dto.request.CreateBonReq;
import com.fade.bon.dto.request.FindBonCommentRequest;
import com.fade.bon.dto.request.FindBonRequest;
import com.fade.bon.dto.response.*;
import com.fade.bon.service.BonService;
import com.fade.bon.dto.response.FindBonCommentResponse;
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
import org.springframework.web.bind.annotation.*;

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
                    content = @Content(schema = @Schema(implementation = CreateBonRes.class))
            )
    )
    public CreateBonRes createBon(
            @Valid @RequestBody CreateBonReq createBonReq,
            @AuthenticationPrincipal UserVo userVo
    ) {
        final var bonId = this.bonService.createBon(userVo.getId(), createBonReq);

        return new CreateBonRes(bonId);
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
                    content = @Content(schema = @Schema(implementation = DeleteBonCommentRes.class))
            )
    )
    public DeleteBonCommentRes deleteBonComment(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("commentId") Long commentId,
            @PathVariable("bonId") Long bonId
    ) {
        this.bonService.deleteBonComment(userVo.getId(), commentId);

        return new DeleteBonCommentRes(commentId);
    }

    @DeleteMapping("{bonId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = DeleteBonRes.class))
            )
    )
    public DeleteBonRes deleteBon(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("bonId") Long bonId
    ) {
        this.bonService.deleteBon(userVo.getId(), bonId);

        return new DeleteBonRes(bonId);
    }

    @PostMapping("{bonId}/comment/{commentId}/like")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = CreateBonCommentLikeRes.class))
            )
    )
    public CreateBonCommentLikeRes createBonCommentLike(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("commentId") Long commentId,
            @PathVariable("bonId") Long bonId
    ) {
        this.bonService.createBonCommentLike(userVo.getId(), commentId);

        return new CreateBonCommentLikeRes(commentId);
    }

    @GetMapping("")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindBonResponse.class))
            )
    )
    public FindBonResponse findBons(
            @AuthenticationPrincipal UserVo userVo,
            @Valid FindBonRequest findBonRequest
    ) {
        return bonService.findBons(userVo.getId(), findBonRequest);
    }

    @GetMapping("{bonId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindBonDetailResponse.class))
            )
    )
    public FindBonDetailResponse findBonDetail(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("bonId") Long bonId
    ) {
        return this.bonService.findBonDetail(userVo.getId(), bonId);
    }

    @GetMapping("{bonId}/comment")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindBonCommentResponse.class))
            )
    )
    public FindBonCommentResponse findBonComments(
            @AuthenticationPrincipal UserVo userVo,
            @PathVariable("bonId") Long bonId,
            @Valid FindBonCommentRequest findBonCommentRequest
    ) {
        return this.bonService.findBonComments(userVo.getId(), bonId, findBonCommentRequest);
    }
}
