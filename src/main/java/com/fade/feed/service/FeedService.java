package com.fade.feed.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.category.dto.response.FindCategoryListResponse;
import com.fade.category.service.CategoryCommonService;
import com.fade.feed.dto.request.CreateFeedRequest;
import com.fade.feed.dto.request.FindFeedRequest;
import com.fade.feed.dto.response.FindFeedResponse;
import com.fade.feed.entity.Feed;
import com.fade.feed.entity.FeedOutfit;
import com.fade.feed.repository.FeedRepository;
import com.fade.member.service.MemberCommonService;
import com.fade.style.service.StyleCommonService;
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

    @Transactional
    public Long createFeed(
            Long memberId,
            CreateFeedRequest createFeedRequest
    ) {
        final var member = this.memberCommonService.findById(memberId);

        final var feedOutfits = createFeedRequest.outfits().stream().map(outfitItem ->
            new FeedOutfit(
                    outfitItem.brandName(),
                    outfitItem.productName(),
                    this.categoryCommonService.findById(outfitItem.categoryId())
            )
        ).toList();
        final var styles = createFeedRequest.styleIds().stream().map(this.styleCommonService::findById).toList();

        System.out.println("feedOutfits" + feedOutfits);

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

        return new FindFeedResponse(
                feeds.stream().map((feed) -> new FindFeedResponse.FindFeedItemResponse(
                        feed.getId(),
                        this.attachmentService.getUrl(
                                feed.getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        feed.getStyles().stream().map((style) -> new FindFeedResponse.FindFeedStyleResponse(
                                style.getId(),
                                style.getName()
                        )).toList(),
                        feed.getFeedOutfitList().stream().map((feedOutfit) -> new FindFeedResponse.FindFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getProductName(),
                                new FindCategoryListResponse.FindCategoryItemResponse(
                                        feedOutfit.getCategory().getId(),
                                        feedOutfit.getCategory().getName()
                                )
                        )).toList(),
                        feed.getMember().getId()
                )).toList(),
                !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getId() : null
        );
    }
}
