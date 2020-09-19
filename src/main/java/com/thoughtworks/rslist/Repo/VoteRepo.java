package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VoteRepo extends CrudRepository<VoteEntity, Integer> {
  Optional<List<VoteEntity>> findAllByUserId (int userId);

  Optional<List<VoteEntity>> findAllByResearchId(int researchId);

  List<VoteEntity> findAllByVoteTimeBetween(LocalDateTime startTime, LocalDateTime endTime);
}
