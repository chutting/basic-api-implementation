package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserControllerTest {
  @Autowired
  MockMvc mockMvc;

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




  private void verifyInvalidUserFields(User user) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    String userJsonString = objectMapper.writeValueAsString(user);
    mockMvc.perform(post("/user/register")
        .content(userJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }
}