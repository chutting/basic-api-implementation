package com.thoughtworks.rslist.controller;

import com.thoughtworks.rslist.api.Research;
import com.thoughtworks.rslist.api.User;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
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

  @GetMapping("/rs/list")
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

  @GetMapping("/rs/findByIndex/{id}")
  public ResponseEntity<ResearchEntity> getResearchByIndex(@PathVariable int id) {

    return ResponseEntity.ok(researchService.findResearchById(id));
  }

  @PostMapping("/rs/add")
  @Transactional
  public ResponseEntity addResearch(@RequestBody @Valid Research research) {
    if (!userService.isExisted(research.getUser())) {
      return ResponseEntity.badRequest().build();
    }

    ResearchEntity researchEntity = researchService.save(research);

    return ResponseEntity.created(null).header("index", String.valueOf(researchEntity.getId())).build();
  }

  @PostMapping("/rs/vote/{rsEventId}")
  @Transactional
  public ResponseEntity vote(@PathVariable int rsEventId,
                             @RequestBody Map<String, String> voteJsonMap) {

    boolean isVoteSuccess = userService.vote(
        Integer.parseInt(voteJsonMap.get("userId")),
        Integer.parseInt(voteJsonMap.get("voteNum")),
        rsEventId);

    ResearchEntity researchEntity = researchService.findResearchById(rsEventId);
    UserEntity userEntity = userService.findUserById(Integer.parseInt(voteJsonMap.get("userId")));

    LocalDateTime localDateTime = convertTimeStringToLocalDateTime(voteJsonMap.get("voteTime"));

    if (isVoteSuccess) {
      VoteEntity newVoteEntity = VoteEntity.builder()
          .research(researchEntity)
          .user(userEntity)
          .voteNum(Integer.parseInt(voteJsonMap.get("voteNum")))
          .voteTime(localDateTime)
          .build();
      voteService.addVoteRecord(newVoteEntity);
      return ResponseEntity.created(null).build();
    }
    return ResponseEntity.badRequest().build();
  }

  private LocalDateTime convertTimeStringToLocalDateTime(String timeString) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(timeString, dateFormat);
  }

  @PutMapping("/rs/modify/{id}")
  public void modifyResearch(@PathVariable int id, @RequestBody Research research) {

    if (!research.getName().isEmpty()) {
      researchService.updateNameById(research.getName(), id);
    }
    if (!research.getKeyword().isEmpty()) {
      researchService.updateKeywordById(research.getKeyword(), id);
    }

  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity handleAddArgumentNotValidException(Exception e) {
    ErrorComment errorComment = new ErrorComment("invalid param");
    logger.error(errorComment.getError());
    return ResponseEntity.badRequest().body(errorComment);
  }

  @DeleteMapping("/rs/delete/{id}")
  public void deleteResearch(@PathVariable int id) {
    researchService.deleteById(id);
  }

  @PatchMapping("/rs/patch")
  public void patchUpdateResearch(@RequestBody Map<String, String> jsonMap) {
    String userId = jsonMap.get("userId");

    if (jsonMap.containsKey("eventName")) {
      researchService.updateNameByUserId(jsonMap.get("eventName"), userId);
    }

    if (jsonMap.containsKey("keyword")) {
      researchService.updateKeywordByUserId(jsonMap.get("keyword"), userId);
    }
  }
}
