package com.fade.attachment.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;

public record GeneratePresignURLRequest(
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, description = "파일 checksum")
        @Nullable
        String checksum
) {

}
