package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepo extends CrudRepository<UserEntity, Integer> {
  @Override
  List<UserEntity> findAll();

  @Query(value = "SELECT COUNT(id) FROM users WHERE name = ?1", nativeQuery = true)
  int ExistedByName(String name);

  @Query(value = "SELECT id FROM users WHERE name = ?1", nativeQuery = true)
  int findIdByName(String name);
}
