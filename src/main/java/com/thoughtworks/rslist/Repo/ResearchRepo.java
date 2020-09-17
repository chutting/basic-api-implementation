package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface ResearchRepo extends CrudRepository<ResearchEntity, Integer> {
  @Override
  List<ResearchEntity> findAll();

  @Transactional
  @Modifying
  @Query(value = "update sys.research set keyword=?1 where user_id=?2", nativeQuery = true)
  int updateKeywordById(String keyword, Integer id);

  @Transactional
  @Modifying
  @Query(value = "update sys.research set name=?1 where user_id=?2", nativeQuery = true)
  int updateNameById(String name, Integer id);

  @Transactional
  @Modifying
  @Query(value = "update sys.research set name=?1,keyword=?2 where user_id=?3", nativeQuery = true)
  int updateNameAndKeywordById(String name, String keyword, Integer id);

  @Transactional
  @Modifying
  @Query(value = "update sys.research set vote_num=?1 where id=?2", nativeQuery = true)
  int updateVoteNumById(Integer voteNum, Integer id);
}
