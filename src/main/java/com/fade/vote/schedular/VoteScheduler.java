package com.fade.vote.schedular;

import com.fade.feed.entity.Feed;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.vote.dto.FindDailyPopularFeedDto;
import com.fade.vote.entity.DailyPopularFeed;
import com.fade.vote.repository.DailyPopularFeedRepository;
import com.fade.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoteScheduler {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final VoteRepository voteRepository;
    private final DailyPopularFeedRepository dailyPopularFeedRepository;
    @Scheduled(cron = "0 0 0 * * ?")
    public void createDailyPopularFeed() {
        FindDailyPopularFeedDto dailyPopularFeedDto = voteRepository.findDailyPopularFeed();
        Feed feed = feedCommonService.findById(dailyPopularFeedDto.feedId());
        Member member = memberCommonService.findById(dailyPopularFeedDto.memberId());

        DailyPopularFeed dailyPopularFeed = DailyPopularFeed.builder()
                .feed(feed)
                .member(member)
                .build();
        dailyPopularFeedRepository.save(dailyPopularFeed);
    }
}
