package com.fade.vote.service;

import com.fade.feed.entity.Feed;
import com.fade.member.entity.Member;
import com.fade.vote.constant.VoteType;
import com.fade.vote.dto.request.CreateVoteRequest.CreateVoteItemRequest;
import com.fade.vote.entity.Vote;
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

    private final VoteRepository voteRepository;

    @Transactional
    public void createVote(List<CreateVoteItemRequest> createVoteItemsRequest) {
        LocalDateTime createVotedAt = LocalDateTime.now();
        //TODO:: MemberRepository, FeedRepository


        //TODO:: 일단 피드 존재 검증 후 유저가 해당 피드에 투표를 했다면 중복 투표 예외 던지기
        List<Vote> voteItems = createVoteItemsRequest.stream()
                .map(voteItem -> createVote(voteItem.voteType(), createVotedAt))
                .collect(Collectors.toList());

        voteRepository.saveAll(voteItems);
    }

    private Vote createVote(VoteType voteType, LocalDateTime votedAt) {
        return Vote.builder()
                .voteType(voteType)
                .votedAt(votedAt)
                .build();
    }

    //TODO:: 레포지토리 연결 후 적용
    private Vote createVote(Member member, Feed feed, VoteType voteType, LocalDateTime votedAt) {
        return Vote.builder()
                .member(member)
                .feed(feed)
                .voteType(voteType)
                .votedAt(votedAt)
                .build();
    }
}
