package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.service.VoteService;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class VoteController {
  private final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }


}
