package com.fade.faparchiving.controller;

import com.fade.faparchiving.dto.response.FindFapArchivingResponse;
import com.fade.faparchiving.service.FapArchivingService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("archiving")
@Tags({
        @Tag(name = "Archiving API")
})
public class FapArchivingController {

    private final FapArchivingService fapArchivingService;

    @GetMapping("")
    @SecurityRequirement(name = "access-token")
    @ApiResponses(
            @ApiResponse(
                    responseCode = "200",
                    content = @Content(schema = @Schema(implementation = FindFapArchivingResponse.class)),
                    description = "월별 페이피 아카이빙 조회 성공"
            )
    )
    public List<FindFapArchivingResponse> findFapArchivingItems(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate selectDate) {
        return fapArchivingService.findFapArchivingItems(selectDate);
    }
}
