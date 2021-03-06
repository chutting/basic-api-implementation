package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.exceptions.ErrorComment;
import com.thoughtworks.rslist.exceptions.RequestParamOutOfBoundsException;
import com.thoughtworks.rslist.service.ResearchService;
import com.thoughtworks.rslist.service.UserService;
import com.thoughtworks.rslist.service.VoteService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class RsController {
  private final UserService userService;
  private final ResearchService researchService;
  private final VoteService voteService;

  Logger logger = LogManager.getLogger(getClass());

  public RsController(UserService userService, ResearchService researchService, VoteService voteService) {
    this.userService = userService;
    this.researchService = researchService;
    this.voteService = voteService;
  }

  @GetMapping("/researches")
  public ResponseEntity<List<ResearchEntity>> getRsList(@RequestParam(required = false) Integer start,
                                  @RequestParam(required = false) Integer end) {

    List<ResearchEntity> allResearch = researchService.findAll();

    start = (start == null ? 1 : start);
    end = (end == null ? allResearch.size() : end);

    if (start < 1 || start > end || end > allResearch.size()) {
      throw new RequestParamOutOfBoundsException();
    }

    if (allResearch.size() == 0) {
      return ResponseEntity.ok(allResearch);
    }

    List<ResearchEntity> outputRsList = allResearch.subList(start - 1, end);

    return ResponseEntity.ok(outputRsList);
  }

  @GetMapping("/research/{id}")
  public ResponseEntity<ResearchEntity> getResearchByIndex(@PathVariable int id) {

    return ResponseEntity.ok(researchService.findResearchById(id));
  }

  @PostMapping("/research")
  public ResponseEntity addResearch(@RequestBody @Valid Research research) {
    if (!userService.isExisted(research.getUser())) {
      return ResponseEntity.badRequest().build();
    }

    ResearchEntity researchEntity = researchService.save(research);

    return ResponseEntity.created(null).header("index", String.valueOf(researchEntity.getId())).build();
  }

  @PostMapping("/research/{rsEventId}/vote")
  public ResponseEntity vote(@PathVariable int rsEventId,
                             @RequestBody Map<String, String> voteJsonMap) {

    int userId = Integer.parseInt(voteJsonMap.get("userId"));
    int voteNum = Integer.parseInt(voteJsonMap.get("voteNum"));

    String voteTime = "";
    if (voteJsonMap.containsKey("voteTime")) {
      voteTime = voteJsonMap.get("voteTime");
    } else {
      voteTime = LocalDateTime.now().toString();
    }


    boolean isVoteSuccess = userService.vote(userId, voteNum, rsEventId);

    if (isVoteSuccess) {
      voteService.addVoteRecord(userId, voteNum, rsEventId, voteTime);
      return ResponseEntity.created(null).build();
    }
    return ResponseEntity.badRequest().build();
  }

  @PutMapping("/research/{id}")
  public ResponseEntity modifyResearch(@PathVariable int id, @RequestBody Research research) {
    researchService.updateById(research, id);
    return ResponseEntity.noContent().build();
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleAddArgumentNotValidException(Exception e) {
    ErrorComment errorComment = new ErrorComment("invalid param");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }

  @DeleteMapping("/research/{id}")
  public ResponseEntity deleteResearch(@PathVariable int id) {
    researchService.deleteById(id);
    return ResponseEntity.noContent().build();
  }

  @PatchMapping("/researches")
  public ResponseEntity patchUpdateResearch(@RequestBody Map<String, String> jsonMap) {
    researchService.updateByUserId(jsonMap);
    return ResponseEntity.noContent().build();
  }
}
