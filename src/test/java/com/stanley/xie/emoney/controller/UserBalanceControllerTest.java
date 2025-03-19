package com.stanley.xie.emoney.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.payload.TopUpBalanceRequest;
import com.stanley.xie.emoney.service.UserBalanceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

    @Test
    void should_TopUpUserBalance_Successfully() throws Exception {
        String payload = getTopUpBalanceRequest();

        mockMvc.perform(post("/user-balance")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful Top Up")));
    }

    @Test
    void should_Failed_TopUpUserBalance_When_InvalidAmount() throws Exception {
        doThrow(InvalidTopUpAmountException.class).when(userBalanceService).topUp("shToken", 10);

        String payload = getTopUpBalanceRequest();

        mockMvc.perform(post("/user-balance")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid top up amount")));
    }

    @Test
    void should_Failed_TopUpUserBalance_When_Unauthorized() throws Exception {
        doThrow(UnauthorizedException.class).when(userBalanceService).topUp("shToken", 10);

        String payload = getTopUpBalanceRequest();

        mockMvc.perform(post("/user-balance")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    private String getTopUpBalanceRequest() throws Exception {
        TopUpBalanceRequest request = new TopUpBalanceRequest(10);
        return objectMapper.writeValueAsString(request);
    }
}