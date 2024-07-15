package com.fade.attachment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record ExistsAttachmentResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "이미 존재하면 true")
        Boolean exists
) {

}
