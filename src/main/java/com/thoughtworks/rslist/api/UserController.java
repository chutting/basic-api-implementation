package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.ErrorComment;
import com.thoughtworks.rslist.service.UserService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@Data
@Slf4j
public class UserController {
  static Logger logger = LogManager.getLogger(UserController.class);

  @Autowired
  private UserService userService;

  public static List<User> userList = new LinkedList<>();
  //代价：UserController中不同的test会互相影响

  @PostMapping("/user/register")
  public ResponseEntity register(@RequestBody @Valid User user) {
//    if (userService.isExisted(user)) {
      userService.save(user);
      return ResponseEntity.created(null).header("index", String.valueOf(userList.size())).build();
//    }

//    if (!userRepo.existsById(userEntity.getId())) {
//      userRepo.save(userEntity);

//    }
//      if (!userList.contains(user)) {
//        userList.add(user);
//        return ResponseEntity.created(null).header("index", String.valueOf(userList.size())).build();
//      }

//      return ResponseEntity.badRequest().build();
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

  @DeleteMapping("/delete/{id}")
  public void deleteById(@PathVariable int id) {
    userService.deleteById(id);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleUserMethodArgumentNotValidException(Exception e) {
    ErrorComment errorComment = new ErrorComment("invalid user");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }
}
