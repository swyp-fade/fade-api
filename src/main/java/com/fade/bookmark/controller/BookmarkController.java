package com.fade.bookmark.controller;

import com.fade.bookmark.service.BookmarkService;
import com.fade.global.dto.response.Response;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    @PostMapping("/{feedId}")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    description = "북마크 성공"
            )
    )
    public Response<Long> addBookmark(Long memberId, @PathVariable Long feedId) {
        return Response.success(bookmarkService.addBookmark(memberId, feedId));
    }

    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "204",
                    description = "북마크 취소"
            )
    )
    @DeleteMapping("/{feedId}")
    public Response<Void> cancelBookmark(Long memberId, @PathVariable Long feedId) {
        bookmarkService.cancelBookmark(memberId, feedId);
        return Response.success();
    }
}