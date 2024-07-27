package com.fade.report.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CountReportRequest {
        @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED)
        private final Long feedId;
}
