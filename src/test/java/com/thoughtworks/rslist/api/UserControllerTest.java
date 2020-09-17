package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.Repo.UserRepo;
import com.thoughtworks.rslist.entity.UserEntity;
import com.thoughtworks.rslist.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
  @Autowired
  MockMvc mockMvc;

  @Autowired
  UserService userService;

  @Test
  void validAddUserToDatabaseSuccessfully() throws Exception {
    User user = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");

    ObjectMapper objectMapper = new ObjectMapper();
    String userJsonString = objectMapper.writeValueAsString(user);
    mockMvc.perform(post("/user/register")
        .content(userJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void shouldGetUserById() throws Exception {
    User userA = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");
    User userB = new User("cttClone", 18, "female", "a@thoughtworks.com", "18888888888");

    ObjectMapper objectMapper = new ObjectMapper();
    String userAJsonString = objectMapper.writeValueAsString(userA);
    String userBJsonString = objectMapper.writeValueAsString(userB);

    mockMvc.perform(post("/user/register")
        .content(userAJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE));

    mockMvc.perform(post("/user/register")
        .content(userBJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE));

    mockMvc.perform(get("/user/2"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", is(2)))
        .andExpect(jsonPath("$.userName", is("cttClone")));
  }

  @Test
  void shouldDeleteUserById() throws Exception {
    User userA = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");
    User userB = new User("cttClone", 18, "female", "a@thoughtworks.com", "18888888888");

    ObjectMapper objectMapper = new ObjectMapper();
    String userAJsonString = objectMapper.writeValueAsString(userA);
    String userBJsonString = objectMapper.writeValueAsString(userB);

    mockMvc.perform(post("/user/register")
        .content(userAJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE));

    mockMvc.perform(post("/user/register")
        .content(userBJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE));

    mockMvc.perform(delete("/delete/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].userName", is("cttClone")));
  }

  @Test
  void shouldNotAddUserWhenSameUserName() throws Exception {
    User userA = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");
    User userB = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");

    ObjectMapper objectMapper = new ObjectMapper();
    String userAJsonString = objectMapper.writeValueAsString(userA);
    String userBJsonString = objectMapper.writeValueAsString(userB);

    mockMvc.perform(post("/user/register")
        .content(userAJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/user/register")
        .content(userBJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());


    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].userName", is("ctt")));
  }

  @Test
  void validUserShouldRegisterSuccessfully() throws Exception {
    User user = new User("ctt", 18, "female", "a@thoughtworks.com", "18888888888");
    ObjectMapper objectMapper = new ObjectMapper();
    String userJsonString = objectMapper.writeValueAsString(user);
    MvcResult mvcResult = mockMvc.perform(post("/user/register")
        .content(userJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    int status = mvcResult.getResponse().getStatus();
    String responseIndex = mvcResult.getResponse().getHeader("index");

    assertEquals(201, status);
    assertEquals("1", responseIndex);
  }

  @Test
  void username_should_be_not_be_null() throws Exception {
    User user = new User(null, 18, "female", "a@thoughtworks.com", "18888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void username_length_should_be_not_over_8() throws Exception {
    User user = new User("123456789", 18, "female", "a@thoughtworks.com", "18888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void gender_should_be_not_be_null() throws Exception {
    User user = new User("1234567", 18, null, "a@thoughtworks.com", "18888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void age_should_in_the_range_of_18_and_100() throws Exception {
    User youngerUser = new User("1234567", 16, "female", "a@thoughtworks.com", "18888888888");
    User olderUser = new User("1234567", 200, "female", "a@thoughtworks.com", "18888888888");

    verifyInvalidUserFields(youngerUser);
    verifyInvalidUserFields(olderUser);
  }

  @Test
  void age_should_not_be_null() throws Exception {
    User user = new User("1234567", null, "female", "a@thoughtworks.com", "18888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void email_should_be_valid() throws Exception {
    User user = new User("1234567", 18, "female", "athoughtworks.com", "18888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void phone_first_number_should_be_1() throws Exception {
    User user = new User("1234567", 18, "female", "a@thoughtworks.com", "28888888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void phone_should_not_be_null() throws Exception {
    User user = new User("1234567", 18, "female", "a@thoughtworks.com", null);

    verifyInvalidUserFields(user);
  }

  @Test
  void phone_number_should_be_11_digits() throws Exception {
    User user = new User("1234567", 18, "female", "a@thoughtworks.com", "188880888888");

    verifyInvalidUserFields(user);
  }

  @Test
  void should_get_all_users_list() throws Exception {
    User userA = new User("ctt", 20, "male", "c@thoughtworks.com", "18888888818");
    User userB = new User("cttClone", 18, "female", "a@thoughtworks.com", "18888888888");

    ObjectMapper objectMapper = new ObjectMapper();
    String userAJsonString = objectMapper.writeValueAsString(userA);
    String userBJsonString = objectMapper.writeValueAsString(userB);

    mockMvc.perform(post("/user/register")
        .content(userAJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(post("/user/register")
        .content(userBJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());

    mockMvc.perform(get("/users"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].userName", is("ctt")))
        .andExpect(jsonPath("$[1].userName", is("cttClone")));
  }

  @Test
  void should_return_400_and_error_comment_when_user_argument_not_valid() throws Exception{
    User user = new User(null, 20, "male", "c@thoughtworks.com", "18888888818");
    ObjectMapper objectMapper = new ObjectMapper();
    String userJsonString = objectMapper.writeValueAsString(user);

    mockMvc.perform(post("/user/register")
        .content(userJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.error", is("invalid user")));
  }

  private void verifyInvalidUserFields(User user) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String userJsonString = objectMapper.writeValueAsString(user);
    mockMvc.perform(post("/user/register")
        .content(userJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }
}