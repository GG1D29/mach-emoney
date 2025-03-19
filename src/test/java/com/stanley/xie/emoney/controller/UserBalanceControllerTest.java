package com.stanley.xie.emoney.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.service.UserBalanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserBalanceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserBalanceService userBalanceService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_GetUserBalance_Successfully() throws Exception {
        Mockito.when(userBalanceService.fetchBalance("shToken")).thenReturn(150);

        mockMvc.perform(get("/user-balance")
                        .header("Authorization", "shToken"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance", is(150)));
    }

    @Test
    void should_Failed_GetUserBalance_When_InvalidToken() throws Exception {
        Mockito.when(userBalanceService.fetchBalance("shToken")).thenThrow(UnauthorizedException.class);

        mockMvc.perform(get("/user-balance")
                        .header("Authorization", "shToken"))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

}