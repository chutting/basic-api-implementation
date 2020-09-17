package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.VoteRepo;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.stereotype.Service;

@Service
public class VoteService {
  private final VoteRepo voteRepo;

  public VoteService(VoteRepo voteRepo) {
    this.voteRepo = voteRepo;
  }

  public VoteEntity addVoteRecord(VoteEntity voteEntity) {
    VoteEntity vote = voteRepo.save(voteEntity);
    return vote;
  }
}
