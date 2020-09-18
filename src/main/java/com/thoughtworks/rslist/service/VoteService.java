package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.Repo.VoteRepo;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class VoteService {
  private final VoteRepo voteRepo;
  private final ResearchRepo researchRepo;
  private final UserRepo userRepo;

  public VoteService(VoteRepo voteRepo, ResearchRepo researchRepo, UserRepo userRepo) {
    this.voteRepo = voteRepo;
    this.researchRepo = researchRepo;
    this.userRepo = userRepo;
  }

  public VoteEntity addVoteRecord(int userId, int voteNum, int researchId, String voteTime) {
    ResearchEntity researchEntity = researchRepo.findById(researchId).get();
    UserEntity userEntity = userRepo.findById(userId).get();

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.parse(voteTime, dateFormat);

    VoteEntity voteEntity = VoteEntity.builder()
        .research(researchEntity)
        .user(userEntity)
        .voteNum(voteNum)
        .voteTime(localDateTime)
        .build();

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
    Optional<List<VoteEntity>> allByResearchId = voteRepo.findAllByResearchId(researchId);
    if (!allByResearchId.isPresent()) {
      return new LinkedList<>();
    }
    return allByResearchId.get();
  }

  public List<VoteEntity> getVotesByTime(String startTime, String endTime) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime startLocalDateTime = LocalDateTime.parse(startTime, dateFormat);
    LocalDateTime endLocalDateTime = LocalDateTime.parse(endTime, dateFormat);

    return voteRepo.findAllByVoteTimeBetween(startLocalDateTime, endLocalDateTime);
  }
}
