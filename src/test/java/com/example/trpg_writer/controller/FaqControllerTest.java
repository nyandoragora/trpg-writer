package com.example.trpg_writer.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FaqControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("よくあるご質問をクリックするとFAQページが表示されること。")
    public void whenGetFaqs_thenReturnsFaqsIndex() throws Exception {
        mockMvc.perform(get("/faqs"))
                .andExpect(status().isOk())
                .andExpect(view().name("faqs/index"));
    }
}

