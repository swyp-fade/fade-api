package com.fade.faparchiving.schedular;

import com.fade.faparchiving.entity.FapArchiving;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.vote.dto.FindMostVoteItemDto;
import com.fade.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class FapArchivingScheduler {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final VoteRepository voteRepository;
    private final FapArchivingRepository fapArchivingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    @Scheduled(cron = "0 0 0 * * ?")
    public void createFapArchiving() {
        FindMostVoteItemDto mostVoteItemDto = voteRepository.findMostVoteItem();

        if (mostVoteItemDto == null) {
            return;
        }

        final var feed = feedCommonService.findById(mostVoteItemDto.feedId());
        final var member = memberCommonService.findById(mostVoteItemDto.memberId());

        FapArchiving fapArchiving = FapArchiving.builder()
                .feed(feed)
                .member(member)
                .build();
        fapArchivingRepository.save(fapArchiving);
        notifyFap(fapArchiving, createFapNotificationDto(member.getId(), feed.getId()));
    }

    private void notifyFap(FapArchiving fapArchiving, CreateNotificationDto createNotificationDto) {
        fapArchiving.publishEvent(eventPublisher, createNotificationDto);
    }

    private CreateNotificationDto createFapNotificationDto(Long receiverId, Long feedId) {
        return CreateNotificationDto.builder()
                .receiverId(receiverId)
                .feedId(feedId)
                .type(NotificationType.FAP_SELECTED)
                .build();
    }
}
