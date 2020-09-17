package com.thoughtworks.rslist.Repo;

import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VoteRepo extends CrudRepository<VoteEntity, Integer> {

}
