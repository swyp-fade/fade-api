package com.fade.feed.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bookmark.dto.request.BookmarkCountRequest;
import com.fade.bookmark.repository.BookmarkRepository;
import com.fade.bookmark.service.BookmarkCommonService;
import com.fade.bookmark.service.BookmarkService;
import com.fade.category.service.CategoryCommonService;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.request.FindNextFeedCursorRequest;
import com.fade.feed.dto.request.ModifyFeedRequest;
import com.fade.feed.dto.response.FindFeedDetailResponse;
import com.fade.feed.dto.response.FindFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.FeedOutfit;
import com.fade.feed.repository.FeedOutfitRepository;
import com.fade.feed.repository.FeedRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.report.dto.request.CountReportRequest;
import com.fade.report.service.ReportService;
import com.fade.style.service.StyleCommonService;
import com.fade.subscribe.service.SubscribeService;
import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.request.CountVoteRequest;
import com.fade.vote.service.VoteService;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {
    private final FeedRepository feedRepository;
    private final FeedCommonService feedCommonService;
    private final FeedOutfitRepository feedOutfitRepository;
    private final MemberCommonService memberCommonService;
    private final BookmarkCommonService bookmarkCommonService;
    private final CategoryCommonService categoryCommonService;
    private final StyleCommonService styleCommonService;
    private final AttachmentService attachmentService;
    private final SubscribeService subscribeService;
    private final BookmarkService bookmarkService;
    private final ApplicationEventPublisher eventPublisher;
    private final FapArchivingRepository fapArchivingRepository;
    private final ReportService reportService;
    private final VoteService voteService;
    private final BookmarkRepository bookmarkRepository;

    @Transactional
    public Long createFeed(
            Long memberId,
            CreateFeedRequest createFeedRequest
    ) {
        final var member = this.memberCommonService.findById(memberId);

        final var feedOutfits = createFeedRequest.outfits().stream().map(outfitItem ->
                new FeedOutfit(
                        outfitItem.brandName(),
                        outfitItem.details(),
                        this.categoryCommonService.findById(outfitItem.categoryId())
                )
        ).toList();

        final var styles = createFeedRequest.styleIds().stream().map(this.styleCommonService::findById).toList();

        final var feed = this.feedRepository.save(new Feed(
                member,
                styles,
                feedOutfits
        ));

        this.attachmentService.linkAttachment(
                createFeedRequest.attachmentId(),
                AttachmentLinkableType.FEED,
                AttachmentLinkType.IMAGE,
                feed.getId()
        );

        return feed.getId();
    }

    @Transactional
    public void modifyFeed(ModifyFeedRequest modifyFeedRequest, Long memberId, Long feedId) {
        final var member = this.memberCommonService.findById(memberId);
        final var feed = this.feedCommonService.findById(feedId);
        final var feedOutfits = this.feedOutfitRepository.findByFeedId(feed.getId());

        if (!member.getId().equals(feed.getMember().getId())) {
            throw new ApplicationException(ErrorCode.FEED_UPDATE_DENIED);
        }

        if (modifyFeedRequest.styleIds() != null) {
            final var styles = modifyFeedRequest.styleIds().stream().map(this.styleCommonService::findById).toList();
            feed.modifyStyles(styles);
        }

        if (modifyFeedRequest.outfits() != null) {
            if (feedOutfits.isPresent()) {
                modifyFeedRequest.outfits().forEach(
                        outfitItem -> {
                            feedOutfits.get().modifyOutfits(
                                    outfitItem.brandName(),
                                    outfitItem.details(),
                                    this.categoryCommonService.findById(outfitItem.categoryId())
                            );
                        }
                );
            } else {
                modifyFeedRequest.outfits().forEach(outfitItem -> {
                            FeedOutfit feedOutfit = new FeedOutfit(
                                    outfitItem.brandName(),
                                    outfitItem.details(),
                                    this.categoryCommonService.findById(outfitItem.categoryId()));
                            feedOutfit.setFeed(feed);
                            this.feedOutfitRepository.save(feedOutfit);
                        }
                );
            }
        }
        this.feedRepository.save(feed);
    }

    public FindFeedResponse findFeeds(@NotNull FindFeedRequest findFeedRequest, @NotNull Long memberId) {
        final var feeds = this.feedRepository.findFeeds(findFeedRequest, memberId);

        final var subscribeMemberIds = this.subscribeService.findSubscribeToMemberIds(memberId);

        return new FindFeedResponse(
                feeds.stream().map((feed) -> new FindFeedResponse.FindFeedItemResponse(
                        feed.getId(),
                        this.attachmentService.getUrl(
                                feed.getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        feed.getStyles().stream().map((style) -> new FindFeedResponse.FindFeedStyleResponse(style.getId())).toList(),
                        feed.getFeedOutfitList().stream().map((feedOutfit) -> new FindFeedResponse.FindFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getDetails(),
                                feedOutfit.getCategory().getId()
                        )).toList(),
                        feed.getMember().getId(),
                        getProfileImageURL(feed.getMember().getId()),
                        !feed.getFapArchivingList().isEmpty(),
                        subscribeMemberIds.contains(feed.getMember().getId()),
                        this.bookmarkService.hasBookmark(memberId, feed.getId()),
                        memberId.equals(feed.getMember().getId()),
                        this.bookmarkService.getCount(BookmarkCountRequest.builder().feedId(feed.getId()).build()),
                        feed.getMember().getUsername(),
                        this.reportService.count(CountReportRequest.builder().feedId(feed.getId()).build()),
                        this.voteService.getCount(
                                CountVoteRequest.builder().
                                        feedId(feed.getId()).
                                        voteType(VoteType.FADE_IN).
                                        build()
                        ),
                        getCreatedAtByFetchType(feed, findFeedRequest.fetchTypes())
                )).toList(),
                findNextCursor(
                        !feeds.isEmpty() ? FindNextFeedCursorRequest.builder()
                                .lastCursor(feeds.get(feeds.size() - 1).getId())
                                .fetchTypes(findFeedRequest.fetchTypes())
                                .targetMemberId(memberId)
                                .build() : null)
        );
    }

    public FindFeedDetailResponse findFeed(Long feedId, Long memberId) {
        final var feed = this.feedRepository.findById(feedId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_FEED));

        return new FindFeedDetailResponse(
                feed.getId(),
                this.attachmentService.getUrl(
                        feed.getId(),
                        AttachmentLinkableType.FEED,
                        AttachmentLinkType.IMAGE
                ),
                feed.getStyles().stream().map((style) -> new FindFeedResponse.FindFeedStyleResponse(style.getId())).toList(),
                feed.getFeedOutfitList().stream().map((feedOutfit) -> new FindFeedResponse.FindFeedOutfitResponse(
                        feedOutfit.getId(),
                        feedOutfit.getBrandName(),
                        feedOutfit.getDetails(),
                        feedOutfit.getCategory().getId()
                )).toList(),
                feed.getMember().getId(),
                getProfileImageURL(feed.getMember().getId()),
                !feed.getFapArchivingList().isEmpty(),
                this.subscribeService.hasSubscribe(memberId, feed.getMember().getId()),
                this.bookmarkService.hasBookmark(memberId, feed.getId()),
                memberId.equals(feed.getMember().getId()),
                this.bookmarkService.getCount(BookmarkCountRequest.builder().feedId(feed.getId()).build()),
                this.voteService.getCount(
                        CountVoteRequest.builder().
                                feedId(feed.getId()).
                                voteType(VoteType.FADE_IN).
                                build()
                ),
                feed.getMember().getUsername(),
                this.reportService.count(CountReportRequest.builder().feedId(feed.getId()).build()),
                feed.getCreatedAt()
        );
    }

    @Transactional
    public void deleteFeed(Long memberId, Long feedId) {
        final var member = memberCommonService.findById(memberId);
        final var feed = this.feedRepository.findById(feedId)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_FEED));

        if (!member.getId().equals(feed.getMember().getId())) {
            throw new ApplicationException(ErrorCode.FEED_DELETE_DENIED);
        }

        feed.remove();

        this.feedRepository.save(feed);

        if (hasFapArchiving(feed.getId())) {
            notifyFapFeedDelete(feed, createFapFeedDeleteDto(member.getId(), feed.getId()));
        }
    }

    private boolean hasFapArchiving(Long feedId) {
        return fapArchivingRepository.existsByFeedId(feedId);
    }

    private void notifyFapFeedDelete(Feed feed, CreateNotificationDto createNotificationDto) {
        feed.publishEvent(eventPublisher, createNotificationDto);
    }

    private CreateNotificationDto createFapFeedDeleteDto(Long receiverId, Long feedId) {
        return CreateNotificationDto.builder()
                .receiverId(receiverId)
                .feedId(feedId)
                .type(NotificationType.FAP_DELETED)
                .build();
    }

    private Long findNextCursor(FindNextFeedCursorRequest findNextFeedCursorRequest) {
        if (findNextFeedCursorRequest == null) {
            return null;
        }

        Feed nextCursorFeed = feedRepository.findNextCursor(findNextFeedCursorRequest);
        if (nextCursorFeed == null) {
            return null;
        }
        return nextCursorFeed.getId();
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

    private LocalDateTime getCreatedAtByFetchType(Feed feed, List<FindFeedRequest.FetchType> fetchTypes) {
        LocalDateTime createdAt = feed.getCreatedAt();

        for (FindFeedRequest.FetchType fetchType : fetchTypes) {
            switch (fetchType) {
                case BOOKMARK:
                    final var bookmark = bookmarkRepository.findByFeedId(feed.getId());
                    createdAt = bookmark.getBookMarkedAt();
                    break;
            }
        }
        return createdAt;
    }
}
