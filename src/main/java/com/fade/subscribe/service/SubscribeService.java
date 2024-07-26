package com.fade.subscribe.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.category.dto.response.FindCategoryListResponse;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.feed.repository.FeedRepository;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.feed.dto.response.FindSubscribeFeedResponse;
import com.fade.subscribe.dto.response.FindSubscriberResponse;
import com.fade.subscribe.entity.Subscribe;
import com.fade.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final MemberCommonService memberCommonService;
    private final SubscribeRepository subscribeRepository;
    private final SubscribeCommonService subscribeCommonService;
    private final FeedRepository feedRepository;
    private final FapArchivingRepository fapArchivingRepository;
    private final AttachmentService attachmentService;

    @Transactional
    public Long subscribe(Long fromMemberId, Long toMemberId) {
        Member fromMember = memberCommonService.findById(fromMemberId);
        Member toMember = memberCommonService.findById(toMemberId);

        Subscribe subscribeFromMember = Subscribe.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .build();

        subscribeRepository.save(subscribeFromMember);

        return subscribeFromMember.getId();
    }

    @Transactional
    public void unSubscribe(Long fromMemberId, Long toMemberId) {
        Subscribe subscribe = subscribeCommonService.findByFromMemberIdAndToMemberId(fromMemberId, toMemberId);

        subscribeRepository.delete(subscribe);
    }

    @Transactional(readOnly = true)
    public FindSubscribeFeedResponse findSubscribeFeeds(Long fromMemberId, Long nextCursor, int limit) {
        final var member = memberCommonService.findById(fromMemberId);
        final var subscribes = subscribeRepository.findByFromMemberId(member.getId());
        final var subscribeMemberIds = subscribes.stream()
                .map(subscribe -> subscribe.getToMember().getId())
                .toList();
        final var feeds = feedRepository.findFeedsByMemberIds(subscribeMemberIds, nextCursor, limit);

        return new FindSubscribeFeedResponse(
                feeds.stream().map(feed -> new FindSubscribeFeedResponse.FindSubscribeFeedItemResponse(
                        feed.getId(),
                        this.attachmentService.getUrl(
                                feed.getId(),
                                AttachmentLinkableType.FEED,
                                AttachmentLinkType.IMAGE
                        ),
                        feed.getStyles().stream().map(style -> new FindSubscribeFeedResponse.FindSubscribeFeedStyleResponse(
                                style.getId(),
                                style.getName()
                        )).toList(),
                        feed.getFeedOutfitList().stream().map(feedOutfit -> new FindSubscribeFeedResponse.FindSubscribeFeedOutfitResponse(
                                feedOutfit.getId(),
                                feedOutfit.getBrandName(),
                                feedOutfit.getProductName(),
                                new FindCategoryListResponse.FindCategoryItemResponse(
                                        feedOutfit.getCategory().getId(),
                                        feedOutfit.getCategory().getName()
                                )
                        )).toList(),
                        feed.getMember().getId(),
                        isArchiving(feed.getId())
                )).toList(),
                !feeds.isEmpty() ? feeds.get(feeds.size() - 1).getId() : null
        );
    }

    @Transactional(readOnly = true)
    public FindSubscriberResponse findSubscribers(Long memberId, Long nextCursor, int limit) {
        final var member = memberCommonService.findById(memberId);
        final var subscribers = subscribeRepository.findSubscribersUsingNoOffset(member.getId(), nextCursor, limit);

        return new FindSubscriberResponse(
                subscribers.stream().map(subscriber -> new FindSubscriberResponse.FindSubscriberItemResponse(
                        subscriber.getToMember().getId(),
                        subscriber.getToMember().getUsername(),
                        this.attachmentService.getUrl(
                                subscriber.getToMember().getId(),
                                AttachmentLinkableType.USER,
                                AttachmentLinkType.PROFILE
                        )
                )).toList(),
                !subscribers.isEmpty() ? subscribers.get(subscribers.size() - 1).getId() : null,
                subscribers.size()
        );
    }

    private boolean isArchiving(Long feedId) {
        return fapArchivingRepository.existsByFeedId(feedId);
    }
}