package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RsControllerTest {

  @Autowired
  MockMvc mockMvc;

  @Test
  void shouldGetRsListString() throws Exception {
    checkRsListOriginalValue();
  }

  @Test
  void shouldGetResearchByIndex() throws Exception {

    mockMvc.perform(get("/rs/findByIndex/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("第一条事件")))
        .andExpect(jsonPath("$.keyword", is("经济")));

    mockMvc.perform(get("/rs/findByIndex/3").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("第三条事件")))
        .andExpect(jsonPath("$.keyword", is("娱乐")));
  }

  @Test
  void shouldGetResearchListStringByStartIndexAndEndIndex() throws Exception {
    mockMvc.perform(get("/rs/list?start=2&end=3"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第二条事件")))
        .andExpect(jsonPath("$[0].keyword", is("政治")))
        .andExpect(jsonPath("$[1].name", is("第三条事件")))
        .andExpect(jsonPath("$[1].keyword", is("娱乐")));
  }

  @Test
  void shouldGetResearchListStringByStartIndex() throws Exception {
    mockMvc.perform(get("/rs/list?start=2"))
        .andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第二条事件")))
        .andExpect(jsonPath("$[0].keyword", is("政治")))
        .andExpect(jsonPath("$[1].name", is("第三条事件")))
        .andExpect(jsonPath("$[1].keyword", is("娱乐")));
  }

  @Test
  void shouldGetResearchListStringByEndIndex() throws Exception {
    mockMvc.perform(get("/rs/list?end=2"))
        .andExpect(status().isOk())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第一条事件")))
        .andExpect(jsonPath("$[0].keyword", is("经济")))
        .andExpect(jsonPath("$[1].name", is("第二条事件")))
        .andExpect(jsonPath("$[1].keyword", is("政治")));
  }

  @Test
  void shouldCouldAddResearch() throws Exception {
    Research researchWithIndexFour = new Research("第四条事件", "教育");
    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour);

    addResearch(researchIndexFourJsonString);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[3].name", is("第四条事件")))
        .andExpect(jsonPath("$[3].keyword", is("教育")));
  }

  @Test
  void shouldCouldModifyResearchNameAndKeywordByIndex() throws Exception {
    Research researchWithIndexOneModified = new Research("经过修改后的第一条事件", "情感");

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified);

    checkRsListOriginalValue();

    performPut("/rs/modify/1", researchIndexOneJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("经过修改后的第一条事件")))
        .andExpect(jsonPath("$[0].keyword", is("情感")));
  }

  @Test
  void shouldCouldModifyResearchNameByIndex() throws Exception {
    Research researchWithIndexTwoModified = new Research("经过修改后的第二条事件", "");

    String researchIndexTwoJsonStringModified = convertResearchToJsonString(researchWithIndexTwoModified);

    performPut("/rs/modify/2", researchIndexTwoJsonStringModified);

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[1].name", is("经过修改后的第二条事件")))
        .andExpect(jsonPath("$[1].keyword", is("政治")));
  }

  @Test
  void shouldCouldModifyResearchKeywordByIndex() throws Exception {
    Research researchWithIndexThreeModified = new Research("", "军事");

    String researchIndexThreeJsonStringModified = convertResearchToJsonString(researchWithIndexThreeModified);

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

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour);
    String researchIndexFiveJsonString = convertResearchToJsonString(researchWithIndexFive);
    String researchIndexSixJsonString = convertResearchToJsonString(researchWithIndexSix);

    addResearch(researchIndexFourJsonString);
    addResearch(researchIndexFiveJsonString);
    addResearch(researchIndexSixJsonString);

    mockMvc.perform(get("/user/all"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].userName", is("ctt")))
        .andExpect(jsonPath("$[1].userName", is("cttClone")));
  }

  @Test
  void shouldFailWhenUserFieldInvalidWhenAddResearch() throws Exception {
    User userA = new User("ctt", 200, "female", "a@123.com", "1234567891");
    Research researchWithIndexFour = new Research("第四条事件", "教育", userA);

    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour);

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest());
  }



  private void addResearch(String researchJsonString) throws Exception {
    mockMvc.perform(post("/rs/add")
        .content(researchJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated());
  }

  private void checkRsListOriginalValue() throws Exception {
    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].name", is("第一条事件")))
        .andExpect(jsonPath("$[0].keyword", is("经济")))
        .andExpect(jsonPath("$[1].name", is("第二条事件")))
        .andExpect(jsonPath("$[1].keyword", is("政治")))
        .andExpect(jsonPath("$[2].name", is("第三条事件")))
        .andExpect(jsonPath("$[2].keyword", is("娱乐")));
  }

  private void performPut(String url, String jsonString) throws Exception {
    mockMvc.perform(put(url)
        .contentType(MediaType.APPLICATION_JSON_VALUE)
        .content(jsonString))
        .andExpect(status().isOk());
  }

  private String convertResearchToJsonString(Research research) throws Exception {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(research);
  }
}
