package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<UserEntity, Integer> {
  @Override
  List<UserEntity> findAll();

  Optional<UserEntity> findByUserName(String userName);

  Optional<UserEntity> findById(Integer userId);

  @Transactional
  @Modifying
  @Query(value = "update sys.users set vote_num=?1 where id=?2", nativeQuery = true)
  int updateVoteNumById(Integer voteNum, Integer id);
}
