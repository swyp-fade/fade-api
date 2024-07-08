package com.fade.attachment.controller;

import com.fade.attachment.dto.response.GeneratePresignURLResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("attachments")
@Tags({
        @Tag(name = "Attachment API")
})
public class AttachmentController {
    @PostMapping("presign-url")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = GeneratePresignURLResponse.class))
            )
    })
    public GeneratePresignURLResponse generatePresignURL() {
        return null;
    }
}
