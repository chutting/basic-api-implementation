package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.ErrorComment;
import com.thoughtworks.rslist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
public class UserController {
  Logger logger = LogManager.getLogger(UserController.class);

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping("/user")
  public ResponseEntity register(@RequestBody @Valid User user) {
    if (!userService.isExisted(user)) {
      UserEntity saveUserEntity = userService.save(user);

      return ResponseEntity.created(null).header("index", String.valueOf(saveUserEntity.getId())).build();
    }

    return ResponseEntity.badRequest().build();
  }

  @GetMapping("/user/{id}")
  public ResponseEntity<UserEntity> findUserById(@PathVariable int id) {
    UserEntity userById = userService.findUserById(id);
    return ResponseEntity.ok(userById);
  }

  @GetMapping("/users")
  public ResponseEntity<List<UserEntity>> showAllUsers() {
    List<UserEntity> allUsers = userService.findAllUsers();
    return ResponseEntity.ok(allUsers);
  }

  @DeleteMapping("/user/{id}")
  public ResponseEntity deleteById(@PathVariable int id) {
    userService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleUserMethodArgumentNotValidException(Exception e) {
    ErrorComment errorComment = new ErrorComment("invalid user");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }
}
