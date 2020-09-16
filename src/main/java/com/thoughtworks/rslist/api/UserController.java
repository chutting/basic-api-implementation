package com.thoughtworks.rslist.api;

import lombok.Data;
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
  public static void register(@RequestBody @Valid User user) {
    userList.add(user);
  }

  @GetMapping("/user/all")
  public static List<User> showAllUsers() {
    return userList;
  }
}
