package com.fade.bookmark.controller;

import com.fade.bookmark.service.BookmarkService;
import com.fade.global.dto.response.Response;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/{feedId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "북마크 성공"
            )
    )
    public Response<Long> addBookmark(@AuthenticationPrincipal UserVo userVo, @PathVariable Long feedId) {
        return Response.success(bookmarkService.addBookmark(userVo.getId(), feedId));
    }

    @DeleteMapping("/{feedId}")
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "북마크 취소"
            )
    )
    public Response<Void> cancelBookmark(@AuthenticationPrincipal UserVo userVo, @PathVariable Long feedId) {
        bookmarkService.cancelBookmark(userVo.getId(), feedId);
        return Response.success();
    }
}