package com.fade.vote.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.category.dto.response.FindCategoryListResponse;
import com.fade.feed.dto.response.ExtractRandomFeedResponse;
import com.fade.feed.dto.response.FindSubscribeFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.repository.FeedRepository;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.request.CreateVoteRequest.CreateVoteItemRequest;
import com.fade.vote.dto.response.CreateVoteResponse.CreateVoteItemResponse;
import com.fade.vote.dto.response.FindMonthlyPopularFeedArchivingResponse;
import com.fade.vote.dto.response.FindVoteResponse;
import com.fade.vote.dto.response.FindVoteResponse.FindVoteItemResponse;
import com.fade.vote.entity.Vote;
import com.fade.vote.exception.DuplicateVoteException;
import com.fade.vote.repository.DailyPopularFeedArchivingRepository;
import com.fade.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final VoteRepository voteRepository;
    private final FeedRepository feedRepository;
    private final DailyPopularFeedArchivingRepository dailyPopularFeedArchivingRepository;
    private final AttachmentService attachmentService;

    @Transactional(readOnly = true)
    public ExtractRandomFeedResponse extractRandomFeeds(Long memberId) {
        final var member = memberCommonService.findById(memberId);
        final var feeds = feedRepository.extractRandomFeeds(member.getId());

        return new ExtractRandomFeedResponse(
                feeds.stream().map(feed -> new ExtractRandomFeedResponse.ExtractRandomFeedItemResponse(
                        feed.getId(),
                        this.attachmentService.getUrl(
                                feed.getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        feed.getStyles().stream().map(style -> new ExtractRandomFeedResponse.ExtractRandomFeedStyleResponse(
                                style.getId(),
                                style.getName()
                        )).toList(),
                        feed.getFeedOutfitList().stream().map(feedOutfit -> new ExtractRandomFeedResponse.ExtractRandomFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getProductName(),
                                new FindCategoryListResponse.FindCategoryItemResponse(
                                        feedOutfit.getCategory().getId(),
                                        feedOutfit.getCategory().getName()
                                )
                        )).toList(),
                        feed.getMember().getId()
                )).toList()
        );
    }

    @Transactional
    public List<CreateVoteItemResponse> createVote(Long memberId, List<CreateVoteItemRequest> createVoteItemsRequest) {
        final var member = memberCommonService.findById(memberId);

        LocalDateTime createVotedAt = LocalDateTime.now();

        List<Vote> voteItems = createVoteItemsRequest.stream()
                .map(voteItem -> {
                    final var feed = feedCommonService.findById(voteItem.feedId());
                    boolean hasAlreadyVoted = voteRepository.existsByMemberIdAndFeedId(
                            member.getId(), feed.getId());

                    if (hasAlreadyVoted) {
                        throw new DuplicateVoteException();
                    }

                    return createVote(member, feed, voteItem.voteType(), createVotedAt);
                })
                .collect(Collectors.toList());
        voteRepository.saveAll(voteItems);

        return voteItems.stream()
                .map(vote -> new CreateVoteItemResponse(vote.getId()))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public FindVoteResponse findVotes(Long memberId, LocalDate nextCursor, int limit, String scrollType) {
        Member member = memberCommonService.findById(memberId);

        LocalDateTime startDate;
        LocalDateTime endDate;
        String direction;

        switch (scrollType) {
            case "0":
                startDate = nextCursor.minusDays(limit).atStartOfDay();
                endDate = nextCursor.atTime(LocalTime.MAX);
                direction = "down";
                break;
            case "1":
                startDate = nextCursor.atStartOfDay();
                endDate = nextCursor.plusDays(limit).atTime(LocalTime.MAX);
                direction = "up";
                break;
            case "2":
                startDate = nextCursor.minusDays(limit).atStartOfDay();
                endDate = nextCursor.plusDays(limit).atTime(LocalTime.MAX);
                direction = "bothSide";
                break;
            default:
                throw new IllegalArgumentException("Invalid type value: " + scrollType);
        }
        List<FindVoteItemResponse> voteItems = voteRepository.findVoteUsingNoOffset(member.getId(), startDate, endDate);


        //TODO:: 마지막 커서 조회 = 최초 호출 시 값을 저장해 놓고 사용하는 방법 찾기
        Optional<Vote> latestVote = voteRepository.findLatestVoteByMember(member.getId());
        Optional<Vote> oldestVote = voteRepository.findOldestVoteByMember(member.getId());

        return new FindVoteResponse(
                voteItems,
                findCursorToUpScroll(voteItems),
                findCursorToDownScroll(voteItems),
                direction,
                isLastCursorToUpScroll(latestVote, nextCursor, limit),
                isLastCursorToDownScroll(oldestVote, nextCursor, limit));
    }

    @Transactional(readOnly = true)
    public List<FindMonthlyPopularFeedArchivingResponse> findMonthlyPopularFeedArchiving(LocalDate selectedDate) {
        LocalDateTime startOfDate = selectedDate.withDayOfMonth(1).atStartOfDay();
        LocalDateTime endOfDate = selectedDate.withDayOfMonth(selectedDate.lengthOfMonth()).atTime(LocalTime.MAX);

        final var dailyPopularFeeds = this.dailyPopularFeedArchivingRepository.findMonthlyPopularFeedArchiving(startOfDate, endOfDate);

        return dailyPopularFeeds.stream()
                .map(dailyPopularFeed -> new FindMonthlyPopularFeedArchivingResponse(
                        dailyPopularFeed.getFeed().getId(),
                        dailyPopularFeed.getMember().getId(),
                        this.attachmentService.getUrl(
                                dailyPopularFeed.getFeed().getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        )
                )).collect(Collectors.toList());
    }

    private LocalDate findCursorToUpScroll(List<FindVoteItemResponse> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime lastVotedAt = voteItems.get(0).votedAt();

        return lastVotedAt.toLocalDate();
    }

    private LocalDate findCursorToDownScroll(List<FindVoteItemResponse> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime lastVotedAt = voteItems.get(voteItems.size() - 1).votedAt();
        return lastVotedAt.toLocalDate();
    }

    private boolean isLastCursorToUpScroll(Optional<Vote> latestVote, LocalDate nextCursor, int limit) {
        return latestVote.map(
                vote -> vote.getVotedAt().toLocalDate().isBefore(nextCursor.plusDays(limit))).orElse(true);
    }

    private boolean isLastCursorToDownScroll(Optional<Vote> oldestVote, LocalDate nextCursor, int limit) {
        return oldestVote.map(
                vote -> vote.getVotedAt().toLocalDate().isAfter(nextCursor.minusDays(limit))).orElse(true);
    }

    private Vote createVote(Member member, Feed feed, VoteType voteType, LocalDateTime votedAt) {
        return Vote.builder()
                .member(member)
                .feed(feed)
                .voteType(voteType)
                .votedAt(votedAt)
                .build();
    }
}
