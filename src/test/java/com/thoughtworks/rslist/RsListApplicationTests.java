package com.thoughtworks.rslist;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.thoughtworks.rslist.api.Research;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;


@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class RsListApplicationTests {

    @Autowired
    MockMvc mockMvc;

    @Test
    void shouldGetRsList() throws Exception {
        mockMvc.perform(get("/rs/list"))
            .andExpect(status().isOk())
            .andExpect(content().string("1  第一条事件  经济\n2  第二条事件  政治\n3  第三条事件  娱乐\n"));
    }

    @Test
    void shouldGetResearchByIndex() throws Exception {
        Research researchWithIndexOne = new Research("第一条事件", "经济");
        Research researchWithIndexThree = new Research("第三条事件", "娱乐");

        ObjectMapper objectMapper = new ObjectMapper();
        String researchIndexOneJsonString = objectMapper.writeValueAsString(researchWithIndexOne);
        String researchIndexThreeJsonString = objectMapper.writeValueAsString(researchWithIndexThree);

        mockMvc.perform(get("/rs/findByIndex/1").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(researchIndexOneJsonString));

        mockMvc.perform(get("/rs/findByIndex/3").contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(content().json(researchIndexThreeJsonString));
    }
}
