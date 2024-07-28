package com.fade.feed.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bookmark.dto.request.BookmarkCountRequest;
import com.fade.bookmark.service.BookmarkService;
import com.fade.category.service.CategoryCommonService;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.response.FindFeedDetailResponse;
import com.fade.feed.dto.response.FindFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.FeedOutfit;
import com.fade.feed.repository.FeedRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.service.MemberCommonService;
import com.fade.notification.constant.NotificationType;
import com.fade.notification.dto.CreateNotificationDto;
import com.fade.style.service.StyleCommonService;
import com.fade.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FeedService {
    private final FeedRepository feedRepository;
    private final MemberCommonService memberCommonService;
    private final CategoryCommonService categoryCommonService;
    private final StyleCommonService styleCommonService;
    private final AttachmentService attachmentService;
    private final SubscribeService subscribeService;
    private final BookmarkService bookmarkService;
    private final ApplicationEventPublisher eventPublisher;
    private final FapArchivingRepository fapArchivingRepository;

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

    public FindFeedResponse findFeeds(FindFeedRequest findFeedRequest, Long memberId) {
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
                        !feed.getFapArchivingList().isEmpty(),
                        subscribeMemberIds.contains(feed.getMember().getId()),
                        this.bookmarkService.hasBookmark(memberId, feed.getId()),
                        memberId.equals(feed.getMember().getId()),
                        this.bookmarkService.getCount(BookmarkCountRequest.builder().feedId(feed.getId()).build()),
                        feed.getMember().getUsername()
                )).toList(),
                !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getId() : null
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
                !feed.getFapArchivingList().isEmpty(),
                this.subscribeService.hasSubscribe(memberId, feed.getMember().getId()),
                this.bookmarkService.hasBookmark(memberId, feed.getId()),
                memberId.equals(feed.getMember().getId()),
                this.bookmarkService.getCount(BookmarkCountRequest.builder().feedId(feed.getId()).build()),
                feed.getMember().getUsername()
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
}
