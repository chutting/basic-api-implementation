package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Integer> {
  @Override
  List<UserEntity> findAll();

  Optional<Integer> findIdByUserName(String userName);
}
