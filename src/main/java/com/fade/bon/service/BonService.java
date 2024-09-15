package com.fade.bon.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bon.dto.request.CreateBonReqDto;
import com.fade.bon.entity.Bon;
import com.fade.bon.repository.BonRepository;
import com.fade.member.service.MemberCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BonService {
    private final BonRepository bonRepository;
    private final AttachmentService attachmentService;
    private final MemberCommonService memberCommonService;

    @Transactional
    public Long createBon(
            Long userId,
            CreateBonReqDto createBonReqDto
    ) {
        final var member = this.memberCommonService.findById(userId);

        final var bon = this.bonRepository.save(new Bon(
                member,
                createBonReqDto.title(),
                createBonReqDto.contents()
        ));

        this.attachmentService.linkAttachment(
                createBonReqDto.attachmentId(),
                AttachmentLinkableType.BON,
                AttachmentLinkType.IMAGE,
                bon.getId()
        );

        return bon.getId();
    }
}
