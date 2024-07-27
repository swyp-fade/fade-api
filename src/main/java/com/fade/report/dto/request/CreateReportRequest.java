package com.fade.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record CreateReportRequest(
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED)
        Long feedId,
        @NotNull
        @Schema(requiredMode = Schema.RequiredMode.REQUIRED, minLength = 1, maxLength = 500)
        @Length(min = 1, max = 500)
        String cause
) {
}
