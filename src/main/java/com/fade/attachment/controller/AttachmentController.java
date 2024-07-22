package com.fade.attachment.controller;

import com.fade.attachment.dto.request.ExistsAttachmentRequest;
import com.fade.attachment.dto.request.GeneratePresignURLRequest;
import com.fade.attachment.dto.response.ExistsAttachmentResponse;
import com.fade.attachment.dto.response.GeneratePresignURLResponse;
import com.fade.attachment.service.AttachmentService;
import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attachments")
@Tags({
        @Tag(name = "Attachment API")
})
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping(
            value = "presign-url"
    )
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = GeneratePresignURLResponse.class))
            )
    })
    public GeneratePresignURLResponse generatePresignURL(
            @RequestBody() @Valid GeneratePresignURLRequest generatePresignURLRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        return this.attachmentService.generatePresignUrl(
                userVo.getId(),
                generatePresignURLRequest.checksum()
        );
    }

    @GetMapping(
            value = "exists"
    )
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ExistsAttachmentResponse.class))
            )
    })
    public ExistsAttachmentResponse existsAttachment(
            @Valid
            ExistsAttachmentRequest existsAttachmentRequest
    ) {
        final var exists = this.attachmentService.existsAttachmentByChecksum(existsAttachmentRequest.checksum());

        return new ExistsAttachmentResponse(exists);
    }
}
