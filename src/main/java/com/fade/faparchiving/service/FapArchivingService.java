package com.fade.faparchiving.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.category.dto.response.FindCategoryListResponse;
import com.fade.faparchiving.dto.response.FindFapArchivingResponse;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.member.service.MemberCommonService;
import com.fade.subscribe.repository.SubscribeRepository;
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

    private final MemberCommonService memberCommonService;
    private final FapArchivingRepository fapArchivingRepository;
    private final AttachmentService attachmentService;
    private final SubscribeRepository subscribeRepository;

    @Transactional(readOnly = true)
    public FindFapArchivingResponse findFapArchivingItems(Long memberId, LocalDate selectedDate) {
        final var member = memberCommonService.findById(memberId);
        LocalDateTime startOfDate = selectedDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfDate = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth()).atTime(LocalTime.MAX);

        final var fapArchivingItems = this.fapArchivingRepository.findFapArchivingItems(startOfDate, endOfDate);

        return new FindFapArchivingResponse(
                fapArchivingItems.stream().map(fapArchivingItem -> new FindFapArchivingResponse.FindFapArchivingItemResponse(
                        fapArchivingItem.getFeed().getId(),
                        this.attachmentService.getUrl(
                                fapArchivingItem.getFeed().getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        fapArchivingItem.getFeed().getStyles().stream().map(style -> new FindFapArchivingResponse.FindFapArchivingStyleResponse(
                                style.getId()
                        )).toList(),
                        fapArchivingItem.getFeed().getFeedOutfitList().stream().map(outfit -> new FindFapArchivingResponse.FindFapArchivingOutfitResponse(
                                outfit.getId(),
                                outfit.getBrandName(),
                                outfit.getProductName(),
                                null
                        )).toList(),
                        fapArchivingItem.getMember().getId(),
                        isSubscribed(member.getId(), fapArchivingItem.getMember().getId()),
                        isMine(member.getId(), fapArchivingItem.getMember().getId())
                )).toList()
        );
    }

    private boolean isSubscribed(Long fromMemberId, Long toMemberId) {
        return subscribeRepository.existsByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
    }

    private boolean isMine(Long memberId, Long fapId) {
        return memberId.equals(fapId);
    }
}
