package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  private final UserRepo userRepo;
  private final ResearchRepo researchRepo;

  public UserService(UserRepo userRepo, ResearchRepo researchRepo) {
    this.userRepo = userRepo;
    this.researchRepo = researchRepo;
  }

  public List<UserEntity> findAllUsers() {
    return userRepo.findAll();
  }

  public UserEntity save(User user) {
    UserEntity userEntity = UserEntity.builder()
        .userName(user.getUserName())
        .age(user.getAge())
        .gender(user.getGender())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .build();
    return userRepo.save(userEntity);
  }

  public UserEntity findUserById(int id) {
    Optional<UserEntity> userEntityOption = userRepo.findById(id);
    if (!userEntityOption.isPresent()) {
      throw new IndexOutOfBoundsException();
    }
    return userRepo.findById(id).get();
  }

  public void deleteById(int id) {
    userRepo.deleteById(id);
  }

  public boolean isExisted(User user) {
    Optional<UserEntity> userEntity = userRepo.findByUserName(user.getUserName());
    return userEntity.isPresent();
  }

  @Transactional
  public boolean vote(int userId, int voteNum, int researchId) {
    Optional<UserEntity> userById = userRepo.findById(userId);
    Optional<ResearchEntity> researchById = researchRepo.findById(researchId);

    if (!userById.isPresent() || userById.get().getVoteNum() < voteNum || !researchById.isPresent()) {
      return false;
    }

    UserEntity userEntity = userById.get();
    int originalVoteNum = userEntity.getVoteNum();
    userEntity.setVoteNum(originalVoteNum - voteNum);

    userRepo.save(userEntity);

    return true;
  }
}
