package com.fade.bon.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bon.dto.request.CreateBonCommentReq;
import com.fade.bon.dto.request.CreateBonReq;
import com.fade.bon.entity.Bon;
import com.fade.bon.entity.BonComment;
import com.fade.bon.entity.BonCommentLike;
import com.fade.bon.repository.BonCommentLikeRepository;
import com.fade.bon.repository.BonCommentRepository;
import com.fade.bon.repository.BonRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
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
    private final BonCommentRepository bonCommentRepository;
    private final BonCommonService bonCommonService;
    private final BonCommentLikeRepository bonCommentLikeRepository;

    @Transactional
    public Long createBon(
            Long memberId,
            CreateBonReq createBonReq
    ) {
        final var member = this.memberCommonService.findById(memberId);

        final var bon = this.bonRepository.save(new Bon(
                member,
                createBonReq.title(),
                createBonReq.contents()
        ));

        this.attachmentService.linkAttachment(
                createBonReq.attachmentId(),
                AttachmentLinkableType.BON,
                AttachmentLinkType.IMAGE,
                bon.getId()
        );

        return bon.getId();
    }

    @Transactional
    public Long createBonComment(Long memberId, Long bonId, CreateBonCommentReq createBonCommentReq) {
        final var member = this.memberCommonService.findById(memberId);
        final var bon = this.bonCommonService.findById(bonId);

        if (this.existsBonCommentByUser(memberId, bonId)) {
            throw new ApplicationException(ErrorCode.EXISTS_BON_COMMENT_BY_USER);
        }

        final var bonComment = this.bonCommentRepository.save(new BonComment(
                member,
                createBonCommentReq.content(),
                bon
        ));

        return bonComment.getId();
    }

    public boolean existsBonCommentByUser(Long memberId, Long bonId) {
        return this.bonCommentRepository.existsByIdAndMemberId(bonId, memberId);
    }

    @Transactional
    public void deleteBonComment(Long memberId, Long commentId) {
        final var bonComment = this.bonCommonService.bonCommentFindById(commentId);

        if (!bonComment.getMember().getId().equals(memberId)) {
            throw new ApplicationException(ErrorCode.REMOVE_BON_COMMENT_FORBIDDEN);
        }

        this.bonCommentRepository.delete(bonComment);
    }

    @Transactional
    public void deleteBon(Long memberId, Long bonId) {
        final var bon = this.bonCommonService.findById(bonId);

        if (!bon.getMember().getId().equals(memberId)) {
            throw new ApplicationException(ErrorCode.REMOVE_BON_FORBIDDEN);
        }

        this.bonRepository.delete(bon);
    }

    @Transactional
    public Long createBonCommentLike(Long memberId, Long bonCommentId) {
        if (this.existsBonCommentLike(memberId, bonCommentId)) {
            throw new ApplicationException(ErrorCode.EXISTS_BON_COMMENT_LIKE);
        }

        final var member = this.memberCommonService.findById(memberId);
        final var bonComment = this.bonCommonService.bonCommentFindById(bonCommentId);

        final var bonCommentLike = this.bonCommentLikeRepository.save(
            new BonCommentLike(
                member,
                bonComment
            )
        );

        return bonCommentLike.getId();
    }

    public boolean existsBonCommentLike(Long memberId, Long bonCommentId) {
        return this.bonCommentLikeRepository.existsByBonCommentIdAndMemberId(bonCommentId, memberId);
    }
}
