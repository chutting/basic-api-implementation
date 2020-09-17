package com.thoughtworks.rslist.service;

import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.swing.text.html.parser.Entity;
import java.util.List;

@Service
public class UserService {
  @Autowired
  private UserRepo userRepo;

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
    return userRepo.findById(id).get();
  }

  public void deleteById(int id) {
    userRepo.deleteById(id);
  }

//  public boolean isExisted(User user) {
//    int count = userRepo.ExistedByName(user.getUserName());
//    return count != 0;
//  }

}
