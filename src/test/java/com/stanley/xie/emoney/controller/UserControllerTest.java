package com.stanley.xie.emoney.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanley.xie.emoney.exception.DuplicateUsernameException;
import com.stanley.xie.emoney.payload.CreateUserRequest;
import com.stanley.xie.emoney.service.UserRegistrationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserRegistrationService userRegistrationService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_RegisterNewUser_Successfully() throws Exception {
        Mockito.when(userRegistrationService.register("shenli")).thenReturn("shToken");

        String payload = getCreateUserRequest();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("shToken")));
    }

    @Test
    void should_Failed_RegisterNewUser_When_UsernameAlreadyExists() throws Exception {
        Mockito.when(userRegistrationService.register("shenli")).thenThrow(DuplicateUsernameException.class);

        String payload = getCreateUserRequest();

        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error", is("Username already exists")));
    }

    private String getCreateUserRequest() throws Exception {
        CreateUserRequest request = new CreateUserRequest("shenli");
        return objectMapper.writeValueAsString(request);
    }

    @Test
    void should_Failed_RegisterNewUser_When_EmptyBody() throws Exception {

        mockMvc.perform(post("/users"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Required request body is missing")));
    }
}