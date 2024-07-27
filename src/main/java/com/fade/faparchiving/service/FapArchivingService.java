package com.fade.faparchiving.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.faparchiving.dto.response.FindFapArchivingResponse;
import com.fade.faparchiving.repository.FapArchivingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FapArchivingService {

    private final FapArchivingRepository fapArchivingRepository;
    private final AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public List<FindFapArchivingResponse> findFapArchivingItems(LocalDate selectedDate) {
        LocalDateTime startOfDate = selectedDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfDate = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth()).atTime(LocalTime.MAX);

        final var fapArchivingItems = this.fapArchivingRepository.findFapArchivingItems(startOfDate, endOfDate);

        return fapArchivingItems.stream()
                .map(fapArchivingItem -> new FindFapArchivingResponse(
                        fapArchivingItem.getFeed().getId(),
                        fapArchivingItem.getMember().getId(),
                        this.attachmentService.getUrl(
                                fapArchivingItem.getFeed().getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        )
                )).collect(Collectors.toList());
    }
}
