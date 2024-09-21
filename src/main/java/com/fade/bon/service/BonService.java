package com.fade.bon.service;

import com.fade.attachment.constant.AttachmentLinkType;
import com.fade.attachment.constant.AttachmentLinkableType;
import com.fade.attachment.service.AttachmentService;
import com.fade.bon.constant.BonVoteType;
import com.fade.bon.dto.request.*;
import com.fade.bon.dto.response.FindBonDetailResponse;
import com.fade.bon.dto.response.FindBonResponse;
import com.fade.bon.entity.*;
import com.fade.bon.repository.*;
import com.fade.bon.dto.response.FindBonCommentResponse;
import com.fade.global.constant.ErrorCode;
import com.fade.global.constant.SearchType;
import com.fade.global.constant.SortType;
import com.fade.global.exception.ApplicationException;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BonService {
    private final BonRepository bonRepository;
    private final AttachmentService attachmentService;
    private final MemberCommonService memberCommonService;
    private final BonCommentRepository bonCommentRepository;
    private final BonVoteRepository bonVoteRepository;
    private final HotBonRepository hotBonRepository;
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

    @Transactional
    public void deleteBonCommentLike(Long memberId, Long bonCommentId) {
        this.bonCommentLikeRepository.deleteByBonCommentIdAndMemberId(bonCommentId, memberId);
    }

    public boolean existsBonCommentLike(Long memberId, Long bonCommentId) {
        return this.bonCommentLikeRepository.existsByBonCommentIdAndMemberId(bonCommentId, memberId);
    }

    public FindBonResponse findBons(Long memberId, FindBonRequest findBonRequest) {
        Member member = this.memberCommonService.findById(memberId);
        if (findBonRequest.sortType().equals(SortType.RECENT)) {
            final var bons = this.bonRepository.findBons(memberId, findBonRequest);

            return new FindBonResponse(
                    bons.stream().map((bon) -> new FindBonResponse.FindBonItemResponse(
                            bon.getId(),
                            bon.getTitle(),
                            this.attachmentService.getUrl(
                                    bon.getId(),
                                    AttachmentLinkableType.BON,
                                    AttachmentLinkType.IMAGE
                            ),
                            countBonVote(VoteCountRequest.builder().bonId(bon.getId()).build()),
                            countBonComment(CommentCountRequest.builder().bonId(bon.getId()).build()),
                            hasVote(member.getId(), bon.getId()),
                            isHot(bon.getId()),
                            isMyBon(bon.getId(), member.getId()),
                            bon.getCreatedAt()
                    )).toList(),
                    findNextCursorFromBon(member.getId(),
                            !bons.isEmpty() ? bons.get(bons.size() - 1).getId() : null,
                            findBonRequest.searchType()));
        }

        final var hotBons = this.hotBonRepository.findHotBons(member.getId(), findBonRequest);

        return new FindBonResponse(
                hotBons.stream().map((hotBon) -> new FindBonResponse.FindBonItemResponse(
                        hotBon.getId(),
                        hotBon.getBon().getTitle(),
                        this.attachmentService.getUrl(
                                hotBon.getBon().getId(),
                                AttachmentLinkableType.BON,
                                AttachmentLinkType.IMAGE
                        ),
                        countBonVote(VoteCountRequest.builder().bonId(hotBon.getBon().getId()).build()),
                        countBonComment(CommentCountRequest.builder().bonId(hotBon.getBon().getId()).build()),
                        hasVote(member.getId(), hotBon.getBon().getId()),
                        isHot(hotBon.getBon().getId()),
                        isMyBon(hotBon.getBon().getId(), member.getId()),
                        hotBon.getBon().getCreatedAt()
                )).toList(),
                findNextCursorFromHotBon(member.getId(),
                        !hotBons.isEmpty() ? hotBons.get(hotBons.size() - 1).getId() : null,
                        findBonRequest.searchType()));
    }

    public FindBonDetailResponse findBonDetail(Long memberId, Long bonId) {
        final var bon = bonCommonService.findById(bonId);

        return new FindBonDetailResponse(
                bon.getTitle(),
                bon.getContent(),
                this.attachmentService.getUrl(
                        bon.getId(),
                        AttachmentLinkableType.BON,
                        AttachmentLinkType.IMAGE
                ),
                countBonVote(VoteCountRequest.builder().bonId(bon.getId()).build()),
                countBonComment(CommentCountRequest.builder().bonId(bon.getId()).build()),
                findVotedValue(bon.getId(), memberId),
                new FindBonDetailResponse.BonCount(
                        calculateBonVoteCount(BonVoteType.YES),
                        calculateBonVoteCount(BonVoteType.NO)
                ),
                hasBonCommented(bon.getId(), memberId)
        );
    }

    public FindBonCommentResponse findBonComments(Long memberId, Long bonId, FindBonCommentRequest findBonCommentRequest) {
        if (findBonCommentRequest.searchType() == SearchType.ALL) {
            final var comments = this.bonCommentRepository.findBonComments(bonId, findBonCommentRequest);

            return new FindBonCommentResponse(
                    comments.stream().map((comment -> new FindBonCommentResponse.FindBonCommentItemResponse(
                            comment.getId(),
                            comment.getContent(),
                            findVotedValue(bonId, comment.getMember().getId()),
                            "anonName",
                            countBonCommentLike(CommentLikeCountRequest.builder().commentId(comment.getId()).build()),
                            hasLike(comment.getId(), memberId),
                            isBestComment(bonId, comment.getId()),
                            isMyComment(comment.getId(), memberId),
                            comment.getCreatedAt()))
                    ).toList(),
                    findNextCursorFromComment(!comments.isEmpty() ? comments.get(comments.size() - 1).getId() : null)
            );
        }

        final var bestComments = this.bonCommentRepository.findBestBonComments(bonId);

        return new FindBonCommentResponse(
                bestComments.stream().map((bestComment -> new FindBonCommentResponse.FindBonCommentItemResponse(
                        bestComment.getId(),
                        bestComment.getContent(),
                        findVotedValue(bonId, bestComment.getMember().getId()),
                        "anonName",
                        countBonCommentLike(CommentLikeCountRequest.builder().commentId(bestComment.getId()).build()),
                        hasLike(bestComment.getId(), memberId),
                        isBestComment(bonId, bestComment.getId()),
                        isMyComment(bestComment.getId(), memberId),
                        bestComment.getCreatedAt()))
                ).toList(),
                null
        );
    }

    private Long countBonVote(VoteCountRequest voteCountRequest) {
        return this.bonVoteRepository.countByCondition(voteCountRequest);
    }

    private Long countBonComment(CommentCountRequest commentCountRequest) {
        return this.bonCommentRepository.countByCondition(commentCountRequest);
    }

    private Long countBonCommentLike(CommentLikeCountRequest commentLikeCountRequest) {
        return this.bonCommentLikeRepository.countByCondition(commentLikeCountRequest);
    }

    private Boolean hasVote(Long bonId, Long memberId) {
        return this.bonVoteRepository.existsByBonIdAndMemberId(bonId, memberId);
    }

    private Boolean hasLike(Long commentId, Long memberId) {
        return this.bonCommentLikeRepository.existsByBonCommentIdAndMemberId(commentId, memberId);
    }

    private Boolean isBestComment(Long bonId, Long commentId) {
        List<BonComment> bestComments = this.bonCommentRepository.findBestBonComments(bonId);
        BonComment bonComment = this.bonCommonService.bonCommentFindById(commentId);

        return bestComments.contains(bonComment);
    }

    private Boolean isHot(Long bonId) {
        return this.hotBonRepository.existsByBonId(bonId);
    }

    private boolean isMyBon(Long bonId, Long memberId) {
        return this.bonRepository.existsByIdAndMemberId(bonId, memberId);
    }

    private boolean isMyComment(Long commentId, Long memberId) {
        return this.bonCommentRepository.existsByIdAndMemberId(commentId, memberId);
    }

    private BonVoteType findVotedValue(Long bonId, Long memberId) {
        BonVote bonVote = this.bonVoteRepository.findByBonIdAndMemberId(bonId, memberId);

        if (bonVote == null) {
            return BonVoteType.NOT;
        }
        return bonVote.getBonVoteType();
    }

    private Long calculateBonVoteCount(BonVoteType bonVoteType) {
        return this.bonVoteRepository.countByCondition(bonVoteType);
    }

    private Boolean hasBonCommented(Long bonId, Long memberId) {
        return this.bonCommentRepository.existsByIdAndMemberId(bonId, memberId);
    }

    private Long findNextCursorFromComment(Long lastCursor) {
        BonComment bonComment = this.bonCommentRepository.findNextCursor(lastCursor);
        if (bonComment == null) {
            return null;
        }
        return bonComment.getId();
    }

    private Long findNextCursorFromBon(Long memberId, Long lastCursor, SearchType searchType) {
        Bon bon = this.bonRepository.findNextCursor(memberId, lastCursor, searchType);
        if (bon == null) {
            return null;
        }
        return bon.getId();
    }

    private Long findNextCursorFromHotBon(Long memberId, Long lastCursor, SearchType searchType) {
        HotBon hotBon = this.hotBonRepository.findNextCursor(memberId, lastCursor, searchType);

        if (hotBon == null) {
            return null;
        }

        return hotBon.getId();
    }
}
