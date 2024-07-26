package com.fade.vote.schedular;

import com.fade.feed.entity.Feed;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.vote.dto.FindDailyPopularFeedDto;
import com.fade.vote.entity.DailyPopularFeedArchiving;
import com.fade.vote.repository.DailyPopularFeedArchivingRepository;
import com.fade.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteScheduler {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final VoteRepository voteRepository;
    private final DailyPopularFeedArchivingRepository dailyPopularFeedArchivingRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyPopularFeed() {
        FindDailyPopularFeedDto dailyPopularFeedDto = voteRepository.findDailyPopularFeed();
        Feed feed = feedCommonService.findById(dailyPopularFeedDto.feedId());
        Member member = memberCommonService.findById(dailyPopularFeedDto.memberId());

        DailyPopularFeedArchiving dailyPopularFeedArchiving = DailyPopularFeedArchiving.builder()
                .feed(feed)
                .member(member)
                .build();
        dailyPopularFeedArchivingRepository.save(dailyPopularFeedArchiving);
        notifyFap(dailyPopularFeedArchiving, createFapNotificationDto(member.getId()));
    }

    //TODO::DailyPopularFeedArchiving 엔티티 -> FapArchiving 으로 수정할 예정
    private void notifyFap(DailyPopularFeedArchiving dailyPopularFeedArchiving, CreateNotificationDto createNotificationDto) {
        dailyPopularFeedArchiving.publishEvent(eventPublisher, createNotificationDto);
    }

    private CreateNotificationDto createFapNotificationDto(Long receiverId) {
        return CreateNotificationDto.builder()
                .receiverId(receiverId)
                .type(NotificationType.FAP_SELECTED)
                .build();
    }
}
