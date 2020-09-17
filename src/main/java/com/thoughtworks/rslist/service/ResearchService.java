package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResearchService {
  private final ResearchRepo researchRepo;

  public ResearchService(ResearchRepo researchRepo) {
    this.researchRepo = researchRepo;
  }

  public ResearchEntity save(Research research) {
    ResearchEntity researchEntity = ResearchEntity.builder()
        .eventName(research.getName())
        .keyword(research.getKeyword())
        .build();

    return researchRepo.save(researchEntity);
  }

  public List<ResearchEntity> findAll() {
    return researchRepo.findAll();
  }

}
