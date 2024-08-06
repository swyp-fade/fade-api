package com.fade.report.controller;

import com.fade.member.constant.MemberRole;
import com.fade.member.vo.UserVo;
import com.fade.report.constant.ReportType;
import com.fade.report.dto.request.CreateReportRequest;
import com.fade.report.dto.response.CreateReportResponse;
import com.fade.report.service.ReportService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("reports")
@Tags(
        @Tag(name = "Report API")
)
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;

    @PostMapping()
    @SecurityRequirement(name = "access-token")
    @Secured(MemberRole.USER_TYPE)
    @ApiResponse(
            responseCode = "200",
            content = @Content(schema = @Schema(implementation = CreateReportResponse.class))
    )
    public CreateReportResponse createReport(
            @Valid CreateReportRequest createReportRequest,
            @AuthenticationPrincipal UserVo userVo
    ) {
        if (createReportRequest.type().equals(ReportType.OTHER)) {
            if (!(createReportRequest.details() != null &&
                    createReportRequest.details().length() > 1 &&
                    createReportRequest.details().length() <= 500)
            ) {
                throw new ValidationException("must be less than or equal to 500");
            }
        }

        final var id = this.reportService.createReport(userVo.getId(), createReportRequest.feedId(), createReportRequest.type(), createReportRequest.details());

        return new CreateReportResponse(id);
    }
}
