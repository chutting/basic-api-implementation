package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.exception.RequestParamOutOfBoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
public class RsController {
  private List<Research> rsList = new ArrayList<>(
      Arrays.asList(
      new Research("第一条事件", "经济", new User("ctt", 18, "female", "a@thoughtworks.com", "12345678901")),
      new Research("第二条事件", "政治", new User("cttClone", 20, "male", "b@thoughtworks.com", "11345678901")),
      new Research("第三条事件", "娱乐", new User("cT", 88, "male", "c@thoughtworks.com", "14345678901"))));

  @GetMapping("/rs/list")
  public ResponseEntity<List<Research>> getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {

    start = (start == null ? 1 : start);
    end = (end == null ? rsList.size() : end);

    if (start < 1 || start > end || end > rsList.size()) {
      throw new RequestParamOutOfBoundException();
    }

    List<Research> outputRsList = rsList.subList(start - 1, end);

    return ResponseEntity.ok(outputRsList);
  }

  @GetMapping("/rs/findByIndex/{id}")
  public ResponseEntity<Research> getResearchByIndex(@PathVariable int id) {
    return ResponseEntity.ok(rsList.get(id - 1));
  }

  @PostMapping("/rs/add")
  public ResponseEntity addResearch(@RequestBody @Valid Research research) {
    User user = research.getUser();
    UserController.register(user);
    rsList.add(research);
    return ResponseEntity.created(null).header("index", String.valueOf(rsList.size())).build();
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
