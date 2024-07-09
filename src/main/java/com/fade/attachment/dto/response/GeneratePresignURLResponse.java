package com.fade.attachment.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record GeneratePresignURLResponse(
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "5분간 업로드 권한 유지")
        String presignURL,
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, description = "파일 고유 키")
        Long attachmentId
) {

}
