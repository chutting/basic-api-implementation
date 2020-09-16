package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;
import java.util.LinkedList;
import java.util.List;

@RestController
public class UserController {

  private List<User> userList = new LinkedList<>();

  @PostMapping("/user/register")
  public void register(@RequestBody @Valid User user) {
    userList.add(user);
  }
}
