package com.fade.feed.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.constant.AttachmentType;
import com.fade.attachment.service.AttachmentService;
import com.fade.category.service.CategoryCommonService;
import com.fade.feed.dto.request.CreateFeedRequest;
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
}
