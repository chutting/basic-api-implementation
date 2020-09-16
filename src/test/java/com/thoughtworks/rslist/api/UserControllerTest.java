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
  void username_should_be_not_be_empty() throws Exception {
    User user = new User(null, 18, "female", "a@thoughtworks.com", "18888888888");

    mockMvc.perform(post("/user/register")
        .content(convertUserToJsonString(user))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  @Test
  void username_length_should_be_not_over_8() throws Exception {
    User user = new User("123456789", 18, "female", "a@thoughtworks.com", "18888888888");

    mockMvc.perform(post("/user/register")
        .content(convertUserToJsonString(user))
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }

  private String convertUserToJsonString(User user) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(user);
  }
}