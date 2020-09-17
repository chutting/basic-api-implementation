package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepo extends CrudRepository<VoteEntity, Integer> {
  Optional<List<VoteEntity>> findAllByUserId (int userId);

  Optional<List<VoteEntity>> findAllByRsEventId(int researchId);
}
