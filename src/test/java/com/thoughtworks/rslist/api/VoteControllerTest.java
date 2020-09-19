package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.Repo.VoteRepo;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.entity.VoteEntity;
import com.thoughtworks.rslist.service.ResearchService;
import com.thoughtworks.rslist.service.VoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VoteControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserRepo userRepo;

  @Autowired
  VoteRepo voteRepo;

  @Autowired
  ResearchRepo researchRepo;

  @Autowired
  ResearchService researchService;

  @Autowired
  VoteService voteService;

  @Test
  void couldGetVoteHistoryByUserId() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    String voteJsonString = "{\"voteNum\": \"5\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"2017-09-28 17:07:05\"" +"}";

    mockMvc.perform(post("/rs/vote/2")
        .content(voteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    String secondVoteJsonString = "{\"voteNum\": \"3\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"2017-09-20 17:07:05\"" +"}";

    mockMvc.perform(post("/rs/vote/3")
        .content(secondVoteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/vote/getVotesByUserId/" + userEntity.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].voteNum", is(5)))
        .andExpect(jsonPath("$[1].voteNum", is(3)));
  }

  @Test
  void couldGetVoteHistoryByResearchId() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    User userClone = new User("cttClone", 18, "female","a@thoughtworks.com", "12345678911");

    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    UserEntity userEntityClone = userRepo.save(convertUserToUserEntity(userClone));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    voteRepo.save(createVoteEntity(userEntity, researchEntity, convertTimeStringToLocalDateTime("2017-09-20 17:07:05"), 5));


    voteRepo.save(createVoteEntity(userEntityClone, researchEntity, convertTimeStringToLocalDateTime("2019-09-20 17:07:05"), 3));

    mockMvc.perform(get("/vote/getVotesByResearchId/" + researchEntity.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].voteNum", is(5)))
        .andExpect(jsonPath("$[1].voteNum", is(3)));

    assertEquals(8, researchService.getVoteSumById(researchEntity.getId()));
  }

  @Test
  void couldGetVoteHistoryByStartAndEndTime() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "体育", user);

    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    voteService.addVoteRecord(userEntity.getId(), 1, researchEntity.getId(), "2017-09-20 17:07:05");
    voteService.addVoteRecord(userEntity.getId(), 1, researchEntity.getId(), "2017-09-23 17:07:05");
    voteService.addVoteRecord(userEntity.getId(), 1, researchEntity.getId(), "2017-09-01 17:07:05");
    voteService.addVoteRecord(userEntity.getId(), 1, researchEntity.getId(), "2017-08-20 17:07:05");
    voteService.addVoteRecord(userEntity.getId(), 1, researchEntity.getId(), "2017-10-20 17:07:05");

    mockMvc.perform(get("/vote/getVotesByTime")
        .param("startTime", "2017-09-01 00:00:00")
        .param("endTime","2017-10-01 00:00:00"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));
  }

  private UserEntity convertUserToUserEntity(User user) {
    return UserEntity.builder()
        .userName(user.getUserName())
        .age(user.getAge())
        .gender(user.getGender())
        .email(user.getEmail())
        .phoneNumber(user.getPhoneNumber())
        .id(user.getId())
        .build();
  }

  private ResearchEntity convertResearchToResearchEntity(Research research) {
    UserEntity userEntity = userRepo.findByUserName(research.getUser().getUserName()).get();
    return ResearchEntity.builder()
        .eventName(research.getName())
        .keyword(research.getKeyword())
        .user(userEntity)
        .build();
  }

  private LocalDateTime convertTimeStringToLocalDateTime(String timeString) {
    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    return LocalDateTime.parse(timeString, dateFormat);
  }

  private VoteEntity createVoteEntity(UserEntity user, ResearchEntity researchEntity, LocalDateTime dateTime, Integer voteNum) {
    return VoteEntity
        .builder()
        .user(user)
        .research(researchEntity)
        .voteTime(dateTime)
        .voteNum(voteNum)
        .build();
  }
}
