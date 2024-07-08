package com.fade.vote.controller;

import com.fade.global.dto.response.Response;
import com.fade.vote.dto.request.VoteRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {

    @PostMapping("/vote")
    public Response<Void> vote(@RequestBody VoteRequest voteRequest) {
        return Response.success();
    }
}
