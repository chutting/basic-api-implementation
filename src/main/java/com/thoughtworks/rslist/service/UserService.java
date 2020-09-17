package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
  final UserRepo userRepo;

  public UserService(UserRepo userRepo) {
    this.userRepo = userRepo;
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
  public boolean vote(int userId, int voteNum) {
    Optional<UserEntity> userById = userRepo.findById(userId);

    if (!userById.isPresent() || userById.get().getVoteNum() < voteNum) {
      return false;
    }
    userRepo.updateVoteNumById(userById.get().getVoteNum() - voteNum, userId);
    return true;
  }
}
