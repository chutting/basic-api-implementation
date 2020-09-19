package com.thoughtworks.rslist;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.service.ResearchService;
import com.thoughtworks.rslist.service.ResearchServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
public class AllConfig {
  private final UserRepo userRepo;

  public AllConfig(UserRepo userRepo, ResearchRepo researchRepo) {
    this.userRepo = userRepo;
    this.researchRepo = researchRepo;
  }

  private final ResearchRepo researchRepo;

  @Bean
  public ResearchService researchService() {
    return new ResearchServiceImpl(userRepo, researchRepo);
  }
}
