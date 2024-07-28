package com.fade.vote.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bookmark.repository.BookmarkRepository;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.dto.response.ExtractRandomFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.repository.FeedRepository;
import com.fade.feed.service.FeedCommonService;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.subscribe.repository.SubscribeRepository;
import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.request.CreateVoteRequest.CreateVoteItemRequest;
import com.fade.vote.dto.response.CreateVoteResponse.CreateVoteItemResponse;
import com.fade.vote.dto.response.FindVoteResponse;
import com.fade.vote.dto.response.FindVoteResponse.FindVoteItemResponse;
import com.fade.vote.entity.Vote;
import com.fade.vote.exception.DuplicateVoteException;
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
    private final BookmarkRepository bookmarkRepository;
    private final SubscribeRepository subscribeRepository;
    private final FapArchivingRepository fapArchivingRepository;
    private final AttachmentService attachmentService;

    //TODO: 카테고리 Id로만 하려면 카테고리 사용하는 곳 다 변경 필요
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
                                style.getId()
                        )).toList(),
                        feed.getFeedOutfitList().stream().map(feedOutfit -> new ExtractRandomFeedResponse.ExtractRandomFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getDetails(),
                                feedOutfit.getCategory().getId()
                        )).toList(),
                        feed.getMember().getId(),
                        isSubscribed(member.getId(), feed.getMember().getId()),
                        isBookmarked(feed.getId(), feed.getMember().getId())
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
        final var voteItems = voteRepository.findVoteUsingNoOffset(member.getId(), startDate, endDate);


        //TODO:: 마지막 커서 조회 = 최초 호출 시 값을 저장해 놓고 사용하는 방법 찾기
        Optional<Vote> latestVote = voteRepository.findLatestVoteByMember(member.getId());
        Optional<Vote> oldestVote = voteRepository.findOldestVoteByMember(member.getId());

        return new FindVoteResponse(
                voteItems.stream().map(voteItem -> new FindVoteItemResponse(
                        voteItem.getId(),
                        voteItem.getVotedAt(),
                        voteItem.getVoteType(),
                        voteItem.getFeed().getId(),
                        this.attachmentService.getUrl(
                                voteItem.getFeed().getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        isFAPFeed(voteItem.getFeed().getId()),
                        isSubscribed(member.getId(), voteItem.getFeed().getMember().getId()),
                        isBookmarked(voteItem.getFeed().getId(), voteItem.getMember().getId()),
                        voteItem.getFeed().getStyles().stream().map(style -> new FindVoteResponse.FindVoteItemStyleResponse(
                                style.getId()
                        )).toList(),
                        voteItem.getFeed().getFeedOutfitList().stream().map(outFit -> new FindVoteResponse.FindVoteItemOutFitResponse(
                                outFit.getId(),
                                outFit.getBrandName(),
                                outFit.getDetails(),
                                null
                        )).toList(),
                        voteItem.getMember().getUsername(),
                        this.attachmentService.getUrl(
                                voteItem.getMember().getId(),
                                AttachmentLinkableType.USER,
                                AttachmentLinkType.PROFILE)
                )).toList(),
                findCursorToUpScroll(voteItems),
                findCursorToDownScroll(voteItems),
                direction,
                isLastCursorToUpScroll(latestVote, nextCursor, limit),
                isLastCursorToDownScroll(oldestVote, nextCursor, limit)
        );
    }

    private LocalDate findCursorToUpScroll(List<Vote> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime lastVotedAt = voteItems.get(0).getVotedAt();

        return lastVotedAt.toLocalDate();
    }

    private LocalDate findCursorToDownScroll(List<Vote> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime lastVotedAt = voteItems.get(voteItems.size() - 1).getVotedAt();
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

    private boolean isSubscribed(Long fromMemberId, Long toMemberId) {
        return subscribeRepository.existsByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
    }

    private boolean isBookmarked(Long feedId, Long memberId) {
        return bookmarkRepository.existsByFeedIdAndMemberId(feedId, memberId);
    }

    private boolean isFAPFeed(Long feedId) {
        return fapArchivingRepository.existsByFeedId(feedId);
    }
}
