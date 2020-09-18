package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ResearchService {
  private final ResearchRepo researchRepo;
  private final UserRepo userRepo;

  public ResearchService(ResearchRepo researchRepo, UserRepo userRepo) {
    this.researchRepo = researchRepo;
    this.userRepo = userRepo;
  }

  public ResearchEntity save(Research research) {
    User user = research.getUser();

    UserEntity userEntity = userRepo.findByUserName(user.getUserName()).get();

    ResearchEntity researchEntity = ResearchEntity.builder()
        .eventName(research.getName())
        .keyword(research.getKeyword())
        .user(userEntity)
        .build();

    return researchRepo.save(researchEntity);
  }

  public List<ResearchEntity> findAll() {
    return researchRepo.findAll();
  }

  public ResearchEntity findResearchById(int researchId) {
    Optional<ResearchEntity> researchOptional = researchRepo.findById(researchId);
    if (!researchOptional.isPresent()) {
      throw new IndexOutOfBoundsException();
    }
    return researchRepo.findById(researchId).get();
  }

  @Transactional
  public void updateByUserId(Map<String, String> jsonMap) {
    int userId = Integer.parseInt(jsonMap.get("userId"));
    Optional<UserEntity> userEntityOptional = userRepo.findById(userId);
    if (!userEntityOptional.isPresent()) {
      throw new RuntimeException();
    }
    UserEntity userEntity = userEntityOptional.get();
    List<ResearchEntity> researchEntityList = researchRepo.findByUser(userEntity);

    if (jsonMap.containsKey("eventName")) {
      String name = jsonMap.get("eventName");
      if (researchEntityList.size() == 0) {
        throw new RuntimeException();
      }
      researchEntityList.forEach(ele -> {
        ele.setEventName(name);
        researchRepo.save(ele);
      });
    }

    if (jsonMap.containsKey("keyword")) {
      String keyword = jsonMap.get("keyword");
      if (researchEntityList.size() == 0) {
        throw new RuntimeException();
      }
      researchEntityList.forEach(ele -> {
        ele.setKeyword(keyword);
        researchRepo.save(ele);
      });
    }
  }

  @Transactional
  public void updateById(Research research, int id) {
    ResearchEntity researchEntity = researchRepo.findById(id).get();
    if (!research.getName().isEmpty()) {
      researchEntity.setEventName(research.getName());
      researchRepo.save(researchEntity);
    }
    if (!research.getKeyword().isEmpty()) {
      researchEntity.setKeyword(research.getKeyword());
      researchRepo.save(researchEntity);
    }
  }

  @Transactional
  public void deleteById(int id) {
    researchRepo.deleteById(id);
  }

  public int getVoteSumById(int id) {
    Optional<ResearchEntity> researchEntityOptional = researchRepo.findById(id);
    if (!researchEntityOptional.isPresent()) {
      throw new IndexOutOfBoundsException();
    }
    List<VoteEntity> voteList = researchEntityOptional.get().getVoteList();
    return voteList.stream().collect(Collectors.summingInt(VoteEntity::getVoteNum));
  }
}
