package com.fade.attachment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;

public record ExistsAttachmentRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "파일 checksum")
        @NotEmpty
        String checksum
) {

}
