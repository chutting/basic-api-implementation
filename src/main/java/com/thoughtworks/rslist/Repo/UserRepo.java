package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<UserEntity, Integer> {
  @Override
  List<UserEntity> findAll();
}
