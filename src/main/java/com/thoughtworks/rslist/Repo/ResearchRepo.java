package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResearchRepo extends CrudRepository<ResearchEntity, Integer> {
  @Override
  List<ResearchEntity> findAll();

  List<ResearchEntity> findByUser(UserEntity userEntity);
}
