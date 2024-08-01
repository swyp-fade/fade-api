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
import com.fade.vote.dto.request.FindVoteRequest;
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
                        isBookmarked(feed.getId(), feed.getMember().getId()),
                        feed.getCreatedAt()
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
    public FindVoteResponse findVotes(Long memberId, FindVoteRequest findVoteRequest) {
        Member member = memberCommonService.findById(memberId);

        LocalDateTime startDate;
        LocalDateTime endDate;
        String direction;

        switch (findVoteRequest.scrollType()) {
            case "0":
                startDate = findVoteRequest.nextCursor().minusDays(findVoteRequest.limit()).atStartOfDay();
                endDate = findVoteRequest.nextCursor().atTime(LocalTime.MAX);
                direction = "down";
                break;
            case "1":
                startDate = findVoteRequest.nextCursor().atStartOfDay();
                endDate = findVoteRequest.nextCursor().plusDays(findVoteRequest.limit()).atTime(LocalTime.MAX);
                direction = "up";
                break;
            case "2":
                startDate = findVoteRequest.nextCursor().minusDays(findVoteRequest.limit()/2).atStartOfDay();
                endDate = findVoteRequest.nextCursor().plusDays(findVoteRequest.limit()/2).atTime(LocalTime.MAX);
                direction = "bothSide";
                break;
            default:
                throw new IllegalArgumentException("Invalid type value: " + findVoteRequest.scrollType());
        }
        final var voteItems = voteRepository.findVoteUsingNoOffset(member.getId(), startDate, endDate);


        Vote latestVote = voteRepository.findLatestVoteByMember(member.getId());
        Vote oldestVote = voteRepository.findOldestVoteByMember(member.getId());

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
                                outFit.getCategory().getId()
                        )).toList(),
                        voteItem.getMember().getUsername(),
                        getProfileImageURL(voteItem.getFeed().getMember().getId())
                )).toList(),
                findCursorToUpScroll(voteItems),
                findCursorToDownScroll(voteItems),
                direction
        );
    }

    private LocalDate findCursorToUpScroll(List<Vote> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime latestVotedAt = voteItems.get(0).getVotedAt();

        Vote nextVoteCursorToUpScroll = voteRepository.findNextUpCursor(latestVotedAt.toLocalDate().atTime(LocalTime.MAX));

        if (nextVoteCursorToUpScroll == null) {
            return null;
        }

        return nextVoteCursorToUpScroll.getVotedAt().toLocalDate();
    }

    private LocalDate findCursorToDownScroll(List<Vote> voteItems) {
        if (voteItems.isEmpty()) {
            return null;
        }
        LocalDateTime oldestVotedAt = voteItems.get(voteItems.size() - 1).getVotedAt();

        Vote nextVoteCursorToDownScroll = voteRepository.findNextDownCursor(oldestVotedAt.toLocalDate().atStartOfDay());

        if (nextVoteCursorToDownScroll == null) {
            return null;
        }
        return nextVoteCursorToDownScroll.getVotedAt().toLocalDate();
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

    private String getProfileImageURL(Long memberId) {
        String profileImageURL = null;

        if (this.attachmentService.existsLinkable(
                memberId,
                AttachmentLinkableType.USER,
                AttachmentLinkType.PROFILE)) {
            profileImageURL = this.attachmentService.getUrl(
                    memberId,
                    AttachmentLinkableType.USER,
                    AttachmentLinkType.PROFILE
            );
        }
        return profileImageURL;
    }
}
