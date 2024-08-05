package com.fade.report.dto.request;

import com.fade.report.constant.ReportType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.jetbrains.annotations.Nullable;

public record CreateReportRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long feedId,
        @Nullable
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, minLength = 1, maxLength = 500)
        String details,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 500)
        ReportType type
) {
}
