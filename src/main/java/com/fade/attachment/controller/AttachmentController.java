package com.fade.attachment.controller;

import com.fade.attachment.dto.request.ExistsAttachmentRequest;
import com.fade.attachment.dto.request.GeneratePresignURLRequest;
import com.fade.attachment.dto.response.ExistsAttachmentResponse;
import com.fade.attachment.dto.response.GeneratePresignURLResponse;
import com.fade.attachment.service.AttachmentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = GeneratePresignURLResponse.class))
            )
    })
    public GeneratePresignURLResponse generatePresignURL(
            @RequestBody() GeneratePresignURLRequest generatePresignURLRequest
    ) {
        return this.attachmentService.genereatePresignUrl(1L, generatePresignURLRequest.checksum());
    }

    @GetMapping(
            value = "exists"
    )
    @SecurityRequirement(name = "access-token")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = ExistsAttachmentResponse.class))
            )
    })
    public ExistsAttachmentResponse existsAttachment(
            ExistsAttachmentRequest existsAttachmentRequest
    ) {
        final var exists = this.attachmentService.existsAttachmentByChecksum(existsAttachmentRequest.checksum());

        return new ExistsAttachmentResponse(exists);
    }
}
