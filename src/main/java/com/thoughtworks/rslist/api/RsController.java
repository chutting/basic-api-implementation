package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.exceptions.ErrorComment;
import com.thoughtworks.rslist.exceptions.RequestParamOutOfBoundsException;
import com.thoughtworks.rslist.service.ResearchService;
import com.thoughtworks.rslist.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@Slf4j
public class RsController {
  private final UserService userService;
  private final ResearchService researchService;

  Logger logger = LogManager.getLogger(getClass());
  private List<Research> rsList = new ArrayList<>(
      Arrays.asList(
      new Research("第一条事件", "经济", new User("ctt", 18, "female", "a@thoughtworks.com", "12345678901")),
      new Research("第二条事件", "政治", new User("cttClone", 20, "male", "b@thoughtworks.com", "11345678901")),
      new Research("第三条事件", "娱乐", new User("cT", 88, "male", "c@thoughtworks.com", "14345678901"))));

  public RsController(UserService userService, ResearchService researchService) {
    this.userService = userService;
    this.researchService = researchService;
  }


  @GetMapping("/rs/list")
  public ResponseEntity<List<ResearchEntity>> getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {

    List<ResearchEntity> allResearch = researchService.findAll();

    start = (start == null ? 1 : start);
    end = (end == null ? allResearch.size() : end);

    if (start < 1 || start > end || end > rsList.size()) {
      throw new RequestParamOutOfBoundsException();
    }

    if (allResearch.size() == 0) {
      return ResponseEntity.ok(allResearch);
    }

    List<ResearchEntity> outputRsList = allResearch.subList(start - 1, end);

    return ResponseEntity.ok(outputRsList);
  }

  @GetMapping("/rs/findByIndex/{id}")
  public ResponseEntity<Research> getResearchByIndex(@PathVariable int id) {
    if (id < 1 || id > rsList.size()) {
      throw new IndexOutOfBoundsException();
    }
    return ResponseEntity.ok(rsList.get(id - 1));
  }

  @PostMapping("/rs/add")
  @Transactional
  public ResponseEntity addResearch(@RequestBody @Valid Research research) {
    if (!userService.isExisted(research.getUser())) {
      return ResponseEntity.badRequest().build();
    }

    ResearchEntity researchEntity = researchService.save(research);

    return ResponseEntity.created(null).header("index", String.valueOf(researchEntity.getId() + 1)).build();
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleAddArgumentNotValidException(Exception e) {
    ErrorComment errorComment = new ErrorComment("invalid param");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }

  @DeleteMapping("/rs/delete/{id}")
  public void deleteResearch(@PathVariable int id) {
    rsList.remove(id - 1);
  }
}
