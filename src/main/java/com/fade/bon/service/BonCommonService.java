package com.fade.bon.service;

import com.fade.bon.entity.Bon;
import com.fade.bon.entity.BonComment;
import com.fade.bon.entity.BonVote;
import com.fade.bon.repository.BonCommentRepository;
import com.fade.bon.repository.BonRepository;
import com.fade.bon.repository.BonVoteRepository;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BonCommonService {
    private final BonRepository bonRepository;
    private final BonCommentRepository bonCommentRepository;
    private final BonVoteRepository bonVoteRepository;

    public Bon findById(Long id) {
        return this.bonRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_BON));
    }

    public BonComment bonCommentFindById(Long id) {
        return this.bonCommentRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_BON_COMMENT));
    }

    public BonVote bonVoteFindById(Long id) {
        return this.bonVoteRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ErrorCode.NOT_FOUND_BON_VOTE));
    }
}
