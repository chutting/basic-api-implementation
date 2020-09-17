package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.VoteRepo;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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

  public List<VoteEntity> findAllByUserId(int userId) {
    Optional<List<VoteEntity>> allByUserId = voteRepo.findAllByUserId(userId);
    if (!allByUserId.isPresent()) {
      return new LinkedList<>();
    }
    return allByUserId.get();
  }

  public List<VoteEntity> findAllByResearchId(int researchId) {
    Optional<List<VoteEntity>> allByResearchId = voteRepo.findAllByRsEventId(researchId);
    if (!allByResearchId.isPresent()) {
      return new LinkedList<>();
    }
    return allByResearchId.get();
  }
}
