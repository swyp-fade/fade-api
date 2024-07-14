package com.fade.vote.service;

import com.fade.feed.entity.Feed;
import com.fade.feed.service.FeedCommonService;
import com.fade.global.constant.ErrorCode;
import com.fade.global.exception.ApplicationException;
import com.fade.member.entity.Member;
import com.fade.member.service.MemberCommonService;
import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.request.CreateVoteRequest.CreateVoteItemRequest;
import com.fade.vote.dto.response.CreateVoteResponse.CreateVoteItemResponse;
import com.fade.vote.entity.Vote;
import com.fade.vote.exception.DuplicateVoteException;
import com.fade.vote.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final MemberCommonService memberCommonService;
    private final FeedCommonService feedCommonService;
    private final VoteRepository voteRepository;

    @Transactional
    public List<CreateVoteItemResponse> createVote(Long memberId, List<CreateVoteItemRequest> createVoteItemsRequest) {
        final var member = memberCommonService.findById(memberId);

        LocalDateTime createVotedAt = LocalDateTime.now();

        List<Vote> voteItems = createVoteItemsRequest.stream()
                .map(voteItem -> {
                    final var feed = feedCommonService.findById(voteItem.feedId());
                    boolean hasAlreadyVoted = voteRepository.existsByMemberIdAndFeedId(
                            member.getId(), feed.getId());

                    if (hasAlreadyVoted) {
                        throw new DuplicateVoteException(ErrorCode.DUPLICATE_VOTE_ERROR);
                    }

                    return createVote(member, feed, voteItem.voteType(), createVotedAt);
                })
                .collect(Collectors.toList());
        voteRepository.saveAll(voteItems);

        return voteItems.stream()
                .map(vote -> new CreateVoteItemResponse(vote.getId()))
                .collect(Collectors.toList());
    }

    private Vote createVote(Member member, Feed feed, VoteType voteType, LocalDateTime votedAt) {
        return Vote.builder()
                .member(member)
                .feed(feed)
                .voteType(voteType)
                .votedAt(votedAt)
                .build();
    }
}
