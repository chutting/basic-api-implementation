package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.service.ResearchService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

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
  ResearchRepo researchRepo;

  @Autowired
  ResearchService researchService;

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
        "                  \"voteTime\": \"current time\"" +"}";

    mockMvc.perform(post("/rs/vote/2")
        .content(voteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    String secondVoteJsonString = "{\"voteNum\": \"3\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"current time\"" +"}";

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

    String voteJsonString = "{\"voteNum\": \"5\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"current time\"" +"}";

    mockMvc.perform(post("/rs/vote/" + researchEntity.getId())
        .content(voteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    String secondVoteJsonString = "{\"voteNum\": \"3\"," +
        "                  \"userId\": " + userEntityClone.getId() + "," +
        "                  \"voteTime\": \"current time\"" +"}";

    mockMvc.perform(post("/rs/vote/" + researchEntity.getId())
        .content(secondVoteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/vote/getVotesByResearchId/" + researchEntity.getId()))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].voteNum", is(5)))
        .andExpect(jsonPath("$[1].voteNum", is(3)));

    assertEquals(8, researchService.getVoteSumById(researchEntity.getId()));
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
}
