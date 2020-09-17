package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

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
        .userId(userEntity.getId())
        .build();

    return researchRepo.save(researchEntity);
  }

  public List<ResearchEntity> findAll() {
    return researchRepo.findAll();
  }

  @Transactional
  public void updateNameById(String name, String id) {
    researchRepo.updateNameById(name, Integer.parseInt(id));
  }

  @Transactional
  public void updateKeywordById(String keyword, String id) {
    researchRepo.updateKeywordById(keyword, Integer.parseInt(id));
  }

  @Transactional
  public void updateNameAndKeywordById(String name, String keyword, String id) {
    researchRepo.updateNameAndKeywordById(name, keyword, Integer.parseInt(id));
  }

}
