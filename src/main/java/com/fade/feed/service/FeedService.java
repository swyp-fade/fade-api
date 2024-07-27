package com.fade.feed.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bookmark.service.BookmarkService;
import com.fade.category.dto.response.FindCategoryListResponse;
import com.fade.category.service.CategoryCommonService;
import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.response.FindFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.FeedOutfit;
import com.fade.feed.repository.FeedRepository;
import com.fade.member.service.MemberCommonService;
import com.fade.style.entity.Style;
import com.fade.style.service.StyleCommonService;
import com.fade.subscribe.service.SubscribeService;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public Long createFeed(
            Long memberId,
            CreateFeedRequest createFeedRequest
    ) {
        final var member = this.memberCommonService.findById(memberId);

        final var feedOutfits = createFeedRequest.outfits().stream().map(outfitItem ->
            new FeedOutfit(
                    outfitItem.brandName(),
                    outfitItem.detail(),
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
                        feed.getStyles().stream().map(Style::getId).toList(),
                        feed.getFeedOutfitList().stream().map((feedOutfit) -> new FindFeedResponse.FindFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getDetail(),
                                new FindCategoryListResponse.FindCategoryItemResponse(
                                        feedOutfit.getCategory().getId(),
                                        feedOutfit.getCategory().getName()
                                )
                        )).toList(),
                        feed.getMember().getId(),
                        !feed.getFapArchivingList().isEmpty(),
                        subscribeMemberIds.contains(feed.getMember().getId()),
                        this.bookmarkService.hasBookmark(memberId, feed.getId()),
                        memberId.equals(feed.getMember().getId())
                )).toList(),
                !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getId() : null
        );
    }
}
