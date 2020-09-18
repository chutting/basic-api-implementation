package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.service.VoteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class VoteController {
  private final VoteService voteService;

  public VoteController(VoteService voteService) {
    this.voteService = voteService;
  }

  @GetMapping("/vote/getVotesByUserId/{userId}")
  public ResponseEntity<List<VoteEntity>> getVotesByUserId(@PathVariable int userId) {
    List<VoteEntity> allVotes = voteService.findAllByUserId(userId);
    return ResponseEntity.ok(allVotes);
  }

  @GetMapping("/vote/getVotesByResearchId/{researchId}")
  public ResponseEntity<List<VoteEntity>> getVotesByResearchId(@PathVariable int researchId) {
    List<VoteEntity> allVotesByResearchId = voteService.findAllByResearchId(researchId);
    return ResponseEntity.ok(allVotesByResearchId);
  }
}
