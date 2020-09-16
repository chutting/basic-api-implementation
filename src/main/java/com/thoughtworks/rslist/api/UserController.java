package com.thoughtworks.rslist.api;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
@Data
public class UserController {

  public static List<User> userList = new LinkedList<>();

  @PostMapping("/user/register")
  public static ResponseEntity register(@RequestBody @Valid User user) {
    userList.add(user);
    return ResponseEntity.created(null).header("index", String.valueOf(userList.size())).build();
  }

  @GetMapping("/user/all")
  public static ResponseEntity<List<User>> showAllUsers() {
    return ResponseEntity.ok(userList);
  }
}
