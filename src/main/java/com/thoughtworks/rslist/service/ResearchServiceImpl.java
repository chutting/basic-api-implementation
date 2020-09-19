package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;

public class ResearchServiceImpl extends ResearchService {
  public ResearchServiceImpl(UserRepo userRepo, ResearchRepo researchRepo) {
    super(userRepo, researchRepo);
  }
}
