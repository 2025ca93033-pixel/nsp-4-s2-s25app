package com.nsp.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nsp.app.model.AskRequest;
import com.nsp.app.service.LLMService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LLMController.class)
class LLMControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LLMService llmService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void health_shouldReturnUp() throws Exception {
        when(llmService.getModelName()).thenReturn("google/flan-t5-large");

        mockMvc.perform(get("/api/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.app").value("NSP-4-S2-S25App"));
    }

    @Test
    void ask_withValidQuestion_shouldReturnAnswer() throws Exception {
        when(llmService.askLLM(any(), any())).thenReturn("Spring Boot is a Java framework.");
        when(llmService.getModelName()).thenReturn("google/flan-t5-large");

        AskRequest request = new AskRequest();
        request.setQuestion("What is Spring Boot?");

        mockMvc.perform(post("/api/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.answer").value("Spring Boot is a Java framework."));
    }

    @Test
    void ask_withBlankQuestion_shouldReturn400() throws Exception {
        AskRequest request = new AskRequest();
        request.setQuestion("  ");

        mockMvc.perform(post("/api/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void ask_withMissingQuestion_shouldReturn400() throws Exception {
        mockMvc.perform(post("/api/ask")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest());
    }
}
