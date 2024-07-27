package com.fade.subscribe.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.faparchiving.repository.FapArchivingRepository;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.subscribe.dto.response.FindSubscriberResponse;
import com.fade.subscribe.entity.Subscribe;
import com.fade.subscribe.repository.SubscribeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final MemberCommonService memberCommonService;
    private final SubscribeRepository subscribeRepository;
    private final SubscribeCommonService subscribeCommonService;
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

    public boolean hasSubscribe(Long fromMemberId, Long toMemberId) {
        return this.subscribeRepository.existsByFromMemberIdAndToMemberId(fromMemberId, toMemberId);
    }

    public List<Long> findSubscribeToMemberIds(Long fromMemberId) {
        return this.subscribeRepository.findByFromMemberToMemberIds(fromMemberId);
    }
}