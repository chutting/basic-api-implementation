package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<Research> rsList = new ArrayList<>(
      Arrays.asList(
      new Research("第一条事件", "经济"),
      new Research("第二条事件", "政治"),
      new Research("第三条事件", "娱乐")));
//  private UserController userController = new UserController();

  @GetMapping("/rs/list")
  public ResponseEntity<List<Research>> getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {

    start = (start == null ? 1 : start);
    end = (end == null ? rsList.size() : end);

    List<Research> outputRsList = rsList.subList(start - 1, end);
    return ResponseEntity.ok(outputRsList);
  }

  @GetMapping("/rs/findByIndex/{id}")
  public Research getResearchByIndex(@PathVariable int id) {
    return rsList.get(id - 1);
  }

  @PostMapping("/rs/add")
  public void addResearch(@RequestBody String researchJsonString) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    Research researchWantToAdd = objectMapper.readValue(researchJsonString, Research.class);
    User user = researchWantToAdd.getUser();

    if (!UserController.showAllUsers().contains(user)) {
      UserController.register(user);
    }
    rsList.add(researchWantToAdd);
  }

  @PutMapping("/rs/modify/{id}")
  public void modifyResearch(@PathVariable int id, @RequestBody Research research) {

    Research researchWantToModified = rsList.get(id - 1);
    if (!research.getName().isEmpty()) {
      researchWantToModified.setName(research.getName());
    }
    if (!research.getKeyword().isEmpty()) {
      researchWantToModified.setKeyword(research.getKeyword());
    }

    rsList.set(id - 1, researchWantToModified);
  }

  @DeleteMapping("/rs/delete/{id}")
  public void deleteResearch(@PathVariable int id) {
    rsList.remove(id - 1);
  }
}
