package com.thoughtworks.rslist.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
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
    Research researchWithIndexOne = new Research("第一条事件", "经济");
    Research researchWithIndexThree = new Research("第三条事件", "娱乐");

    String researchIndexOneJsonString = convertResearchToJsonString(researchWithIndexOne);
    String researchIndexThreeJsonString = convertResearchToJsonString(researchWithIndexThree);

    mockMvc.perform(get("/rs/findByIndex/1").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().json(researchIndexOneJsonString));

    mockMvc.perform(get("/rs/findByIndex/3").contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(content().json(researchIndexThreeJsonString));
  }

  @Test
  void shouldGetResearchListStringByStartIndexAndEndIndex() throws Exception {
    mockMvc.perform(get("/rs/list?start=2&end=3"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第二条事件  政治\n2  第三条事件  娱乐\n"));

    mockMvc.perform(get("/rs/list?start=2"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第二条事件  政治\n2  第三条事件  娱乐\n"));

    mockMvc.perform(get("/rs/list?end=2"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第一条事件  经济\n2  第二条事件  政治\n"));
  }

  @Test
  void shouldCouldAddResearch() throws Exception {
    Research researchWithIndexFour = new Research("第四条事件", "教育");
    String researchIndexFourJsonString = convertResearchToJsonString(researchWithIndexFour);

    checkRsListOriginalValue();

    mockMvc.perform(post("/rs/add")
        .content(researchIndexFourJsonString)
        .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第一条事件  经济\n2  第二条事件  政治\n3  第三条事件  娱乐\n4  第四条事件  教育\n"));
  }

  @Test
  void shouldCouldModifyResearchByIndex() throws Exception {
    Research researchWithIndexOneModified = new Research("经过修改后的第一条事件", "情感");
    Research researchWithIndexTwoModified = new Research("经过修改后的第二条事件", "");
    Research researchWithIndexThreeModified = new Research("", "军事");

    String researchIndexOneJsonStringModified = convertResearchToJsonString(researchWithIndexOneModified);
    String researchIndexTwoJsonStringModified = convertResearchToJsonString(researchWithIndexTwoModified);
    String researchIndexThreeJsonStringModified = convertResearchToJsonString(researchWithIndexThreeModified);

    checkRsListOriginalValue();

    performPut("/rs/modify/1", researchIndexOneJsonStringModified);
    performPut("/rs/modify/2", researchIndexTwoJsonStringModified);
    performPut("/rs/modify/3", researchIndexThreeJsonStringModified);


    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  经过修改后的第一条事件  情感\n2  经过修改后的第二条事件  政治\n3  第三条事件  军事\n"));
  }

  @Test
  void shouldCouldDeleteByIndex() throws Exception {
    checkRsListOriginalValue();

    mockMvc.perform(delete("/rs/delete/1"))
        .andExpect(status().isOk());

    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第二条事件  政治\n2  第三条事件  娱乐\n"));
  }

  private void checkRsListOriginalValue() throws Exception {
    mockMvc.perform(get("/rs/list"))
        .andExpect(status().isOk())
        .andExpect(content().string("1  第一条事件  经济\n2  第二条事件  政治\n3  第三条事件  娱乐\n"));
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