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

import java.util.List;

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

//  public RsControllerTest(UserRepo userRepo, ResearchRepo researchRepo) {
//    this.userRepo = userRepo;
//    this.researchRepo = researchRepo;
//  }

  @BeforeEach
  void deleteAllInDataBase() {
    userRepo.deleteAll();
    researchRepo.deleteAll();
  }

  @Test
  void shouldGetRsListString() throws Exception {
    checkRsListOriginalValue();
  }

  @Test
  void shouldGetResearchByIndex() throws Exception {

    mockMvc.perform(get("/rs/findByIndex/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("第一条事件")))
        .andExpect(jsonPath("$", not(hasKey("user"))));

    mockMvc.perform(get("/rs/findByIndex/3").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("第三条事件")))
        .andExpect(jsonPath("$", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchListStringByStartIndexAndEndIndex() throws Exception {
    mockMvc.perform(get("/rs/list?start=2&end=3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第二条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].name", is("第三条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchListStringByStartIndex() throws Exception {
    mockMvc.perform(get("/rs/list?start=2"))
        .andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第二条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].name", is("第三条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))));
  }

  @Test
  void shouldGetResearchListStringByEndIndex() throws Exception {
    mockMvc.perform(get("/rs/list?end=2"))
        .andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第一条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].name", is("第二条事件")))
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
        .userId(userEntity.getId())
        .build();
  }


  @Test
  void shouldCouldModifyResearchNameAndKeywordByIndex() throws Exception {
    User user = new User("ctt", 18, "female", "a@thoughtworks.com", "12345678911");
    Research researchWithIndexOneModified = new Research("经过修改后的第一条事件", "情感", user);

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified, user);

    checkRsListOriginalValue();

    performPut("/rs/modify/1", researchIndexOneJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("经过修改后的第一条事件")))
        .andExpect(jsonPath("$[0].keyword", is("情感")));
  }

  @Test
  void shouldCouldModifyResearchNameByIndex() throws Exception {
    User user = new User("ctt", 18, "female", "a@thoughtworks.com", "12345678911");
    Research researchWithIndexTwoModified = new Research("经过修改后的第二条事件", "", user);

    String researchIndexTwoJsonStringModified = convertResearchToJsonString(researchWithIndexTwoModified, user);

    performPut("/rs/modify/2", researchIndexTwoJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[1].name", is("经过修改后的第二条事件")))
        .andExpect(jsonPath("$[1].keyword", is("政治")));
  }

  @Test
  void shouldCouldModifyResearchKeywordByIndex() throws Exception {
    User user = new User("ctt", 18, "female", "a@thoughtworks.com", "12345678911");
    Research researchWithIndexThreeModified = new Research("", "军事", user);

    String researchIndexThreeJsonStringModified = convertResearchToJsonString(researchWithIndexThreeModified, user);

    performPut("/rs/modify/3", researchIndexThreeJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[2].name", is("第三条事件")))
        .andExpect(jsonPath("$[2].keyword", is("军事")));
  }

  @Test
  void shouldCouldDeleteByIndex() throws Exception {
    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(3)));

    mockMvc.perform(delete("/rs/delete/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void shouldAddNewUserWhenAddResearch() throws Exception {
    User userA = new User("ctt", 18, "female", "a@thoughtworks.com", "12345678911");
    User userB = new User("cttClone", 19, "male", "c@123.com", "19876543211");

    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);
    Research researchWithIndexFive = new Research("第五条事件", "八卦", userA);
    Research researchWithIndexSix = new Research("第六条事件", "科技", userB);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, userA);
    String researchIndexFiveJsonString = convertResearchToJsonString(researchWithIndexFive, userA);
    String researchIndexSixJsonString = convertResearchToJsonString(researchWithIndexSix, userB);

    addResearchShouldSuccess(researchIndexFourJsonString, "4");
    addResearchShouldSuccess(researchIndexFiveJsonString, "5");
    addResearchShouldSuccess(researchIndexSixJsonString, "6");

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].userName", is("ctt")))
        .andExpect(jsonPath("$[1].userName", is("cttClone")));
  }

  @Test
  void shouldFailWhenUserFieldInvalidWhenAddResearch() throws Exception {
    User userA = new User("ctt", 200, "female", "a@123.com", "1234567891");
    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, userA);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void shouldReturn400AndErrorCommentWhenRequestParamOutOfBounds() throws Exception {
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
    Research researchWithIndexFour = new Research("第四条事件", "教育", user);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour, user);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid param")));
  }

  private void addResearchShouldSuccess(String researchJsonString, String index) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/rs/add")
        .content(researchJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();
    int status = mvcResult.getResponse().getStatus();
    String responseIndex = mvcResult.getResponse().getHeader("index");

    assertEquals(201, status);
    assertEquals(index, responseIndex);
  }

  private void checkRsListOriginalValue() throws Exception {
    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第一条事件")))
        .andExpect(jsonPath("$[0]", not(hasKey("user"))))
        .andExpect(jsonPath("$[1].name", is("第二条事件")))
        .andExpect(jsonPath("$[1]", not(hasKey("user"))))
        .andExpect(jsonPath("$[2].name", is("第三条事件")))
        .andExpect(jsonPath("$[2]", not(hasKey("user"))));
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
}
