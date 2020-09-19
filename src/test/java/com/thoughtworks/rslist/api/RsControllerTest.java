package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Repo.ResearchRepo;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.entity.ResearchEntity;
import com.thoughtworks.rslist.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {
  @Autowired
  UserRepo userRepo;

  @Autowired
  ResearchRepo researchRepo;

  @Autowired
  MockMvc mockMvc;

  @BeforeEach
  void deleteAllInDataBase() {
    userRepo.deleteAll();
    researchRepo.deleteAll();
  }

  @Test
  void shouldGetRsListString() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第四条事件")))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchByIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    mockMvc.perform(get("/rs/findByIndex/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eventName", is("第四条事件")));

    mockMvc.perform(get("/rs/findByIndex/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.eventName", is("第一条事件")));
  }

  @Test
  void shouldGetResearchListStringByStartIndexAndEndIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexTwo = new Research("第二条事件", "经济", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexTwo));

    mockMvc.perform(get("/rs/list?start=2&end=3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchListStringByStartIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexTwo = new Research("第二条事件", "经济", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexTwo));

    mockMvc.perform(get("/rs/list?start=2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第一条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].eventName", is("第二条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchListStringByEndIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexTwo = new Research("第二条事件", "经济", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexTwo));

    mockMvc.perform(get("/rs/list?end=2"))
        .andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第四条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].eventName", is("第一条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))));
  }

  @Test
  void shouldCouldAddResearchWhenUserExists() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, user);

    MvcResult mvcResult = mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    int status = mvcResult.getResponse().getStatus();
    String responseIndex = mvcResult.getResponse().getHeader("index");

    assertEquals(201, status);
    assertEquals("1", responseIndex);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第四条事件")))
        .andExpect(jsonPath("$[0].keyword", is("教育")));
  }

  @Test
  void shouldCouldNotAddResearchWhenUserNotExists() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, user);


    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void deleteAllResearchWhenDeleteUser() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));

    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, user);
    String researchIndexOneJsonString = convertResearchToJsonString(researchWithIndexOne, user);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/rs/add")
        .content(researchIndexOneJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(delete("/user/delete/1"))
        .andExpect(status().isNoContent());

    assertEquals(0, researchRepo.findAll().size());
  }

  @Test
  void shouldPatchUpdate() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    String updateJson = "{\"eventName\": \"新的热搜事件名\"," +
        "                  \"keyword\": \"新的关键字\"," +
        "                  \"userId\": " + userEntity.getId() +"}";

    mockMvc.perform(patch("/rs/patch")
        .content(updateJson)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("新的热搜事件名")))
        .andExpect(jsonPath("$[1].keyword", is("新的关键字")));
  }

  @Test
  void shouldPatchOnlyUpdateKeyword() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    String updateJson = "{ \"keyword\": \"新的关键字\"," +
        "                  \"userId\": " + userEntity.getId() +"}";

    mockMvc.perform(patch("/rs/patch")
        .content(updateJson)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("第四条事件")))
        .andExpect(jsonPath("$[1].keyword", is("新的关键字")));
  }

  @Test
  void shouldPatchOnlyUpdateEventName() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    String updateJson = "{\"eventName\": \"新的热搜事件名\"," +
        "                  \"userId\": " + userEntity.getId() +"}";

    mockMvc.perform(patch("/rs/patch")
        .content(updateJson)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].eventName", is("新的热搜事件名")))
        .andExpect(jsonPath("$[1].keyword", is("情感")));
  }

  @Test
  void could_vote() throws Exception {
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

    assertEquals(5, userRepo.findById(userEntity.getId()).get().getVoteNum());

    String secondVoteJsonString = "{\"voteNum\": \"6\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"2018-09-28 17:07:05\"" +"}";

    mockMvc.perform(post("/rs/vote/3")
        .content(secondVoteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldVoteFailedWhenResearchIdNotExist() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    UserEntity userEntity = userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    String voteJsonString = "{\"voteNum\": \"5\"," +
        "                  \"userId\": " + userEntity.getId() + "," +
        "                  \"voteTime\": \"2017-09-29 17:07:05\"" +"}";

    mockMvc.perform(post("/rs/vote/10")
        .content(voteJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldCouldModifyResearchNameAndKeywordByIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexOneModified = new Research("经过修改后的第一条事件", "经济", user);

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified, user);

    performPut("/rs/modify/" + researchEntity.getId(), researchIndexOneJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[1].eventName", is("经过修改后的第一条事件")))
        .andExpect(jsonPath("$[1].keyword", is("经济")));
  }

  @Test
  void shouldCouldModifyResearchNameByIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexOneModified = new Research("经过修改后的第一条事件", "", user);

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified, user);

    performPut("/rs/modify/" + researchEntity.getId(), researchIndexOneJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[1].eventName", is("经过修改后的第一条事件")))
        .andExpect(jsonPath("$[1].keyword", is("情感")));
  }

  @Test
  void shouldCouldModifyResearchKeywordByIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    Research researchWithIndexOneModified = new Research("", "军事", user);

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified, user);

    performPut("/rs/modify/" + researchEntity.getId(), researchIndexOneJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[1].eventName", is("第一条事件")))
        .andExpect(jsonPath("$[1].keyword", is("军事")));
  }

  @Test
  void shouldCouldDeleteByIndex() throws Exception {
    User user = new User("ctt", 18, "female","a@thoughtworks.com", "12345678911");
    userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    Research researchWithIndexOne = new Research("第一条事件", "情感", user);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));
    ResearchEntity researchEntity = researchRepo.save(convertResearchToResearchEntity(researchWithIndexOne));

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    mockMvc.perform(delete("/rs/delete/" + researchEntity.getId()))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void shouldFailWhenUserFieldInvalidWhenAddResearch() throws Exception {
    User userA = new User("ctt", 200, "female", "a@123.com", "1234567891");
    userRepo.save(convertUserToUserEntity(userA));
    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, userA);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400AndErrorCommentWhenRequestParamOutOfBounds() throws Exception {
    User userA = new User("ctt", 200, "female", "a@123.com", "1234567891");
    userRepo.save(convertUserToUserEntity(userA));
    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    mockMvc.perform(get("/rs/list?start=-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid request param")));

    mockMvc.perform(get("/rs/list?start=2&end=1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid request param")));

    mockMvc.perform(get("/rs/list?end=100"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid request param")));
  }

  @Test
  void shouldReturn400AndErrorCommentWhenIndexOutOfBounds() throws Exception {
    User userA = new User("ctt", 200, "female", "a@123.com", "1234567891");
    userRepo.save(convertUserToUserEntity(userA));
    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);
    researchRepo.save(convertResearchToResearchEntity(researchWithIndexFour));

    mockMvc.perform(get("/rs/findByIndex/-1"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid index")));

    mockMvc.perform(get("/rs/findByIndex/500"))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid index")));
  }

  @Test
  void shouldReturn400AndErrorCommentWhenResearchUserNotValid() throws Exception {
    User user = new User("ctt", 200, "female", "a@123.com", "1234567891");
    userRepo.save(convertUserToUserEntity(user));
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);
    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, user);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid param")));
  }

  private void performPut(String url, String jsonString) throws Exception {
    mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(jsonString))
        .andExpect(status().isOk());
  }

  private String convertResearchToJsonString(Research research, User user) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String jsonString = objectMapper.writeValueAsString(research);

    StringBuilder stringBuilder =new StringBuilder(jsonString.substring(0, jsonString.length() - 1));
    String userJson = objectMapper.writeValueAsString(user);
    StringBuilder output = stringBuilder.append(",\"user\":").append(userJson).append("}");
    return output.toString();
  }

  private ResearchEntity convertResearchToResearchEntity(Research research) {
    UserEntity userEntity = userRepo.findByUserName(research.getUser().getUserName()).get();

    return ResearchEntity.builder()
        .eventName(research.getName())
        .keyword(research.getKeyword())
        .user(userEntity)
        .build();
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

}
