package com.stanley.xie.emoney.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stanley.xie.emoney.exception.InsufficientBalanceException;
import com.stanley.xie.emoney.exception.InvalidTransferAmountException;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.exception.UsernameNotFoundException;
import com.stanley.xie.emoney.payload.MoneyTransferRequest;
import com.stanley.xie.emoney.service.MoneyTransferService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MoneyTransferControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoneyTransferService moneyTransferService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void should_TransferMoney_Successfully() throws Exception {
        String payload = getMoneyTransferRequest();

        mockMvc.perform(post("/money-transfer")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("Successful Money Transfer")));
    }

    @Test
    void should_Failed_TransferMoney_When_InsufficientBalance() throws Exception {
        doThrow(InsufficientBalanceException.class).when(moneyTransferService).transfer("shToken", "shenli", 10);

        String payload = getMoneyTransferRequest();

        mockMvc.perform(post("/money-transfer")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Insufficient balance")));
    }

    @Test
    void should_Failed_TransferMoney_When_BelowMinimumAmount() throws Exception {
        doThrow(InvalidTransferAmountException.class).when(moneyTransferService).transfer(
                "shToken", "shenli", -10);

        String payload = getNegativeMoneyTransferRequest();

        mockMvc.perform(post("/money-transfer")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Invalid transfer amount")));
    }

    @Test
    void should_Failed_TransferMoney_When_Unauthorized() throws Exception {
        doThrow(UnauthorizedException.class).when(moneyTransferService).transfer("shToken", "shenli", 10);

        String payload = getMoneyTransferRequest();

        mockMvc.perform(post("/money-transfer")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    void should_Failed_TransferMoney_When_TargetUserNotFound() throws Exception {
        doThrow(UsernameNotFoundException.class).when(moneyTransferService).transfer("shToken", "shenli", 10);

        String payload = getMoneyTransferRequest();

        mockMvc.perform(post("/money-transfer")
                        .header("Authorization", "shToken")
                        .contentType(MediaType.APPLICATION_JSON).content(payload))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error", is("Username not found")));
    }

    private String getMoneyTransferRequest() throws Exception {
        MoneyTransferRequest request = new MoneyTransferRequest("shenli", 10);
        return objectMapper.writeValueAsString(request);
    }

    private String getNegativeMoneyTransferRequest() throws Exception {
        MoneyTransferRequest request = new MoneyTransferRequest("shenli", -10);
        return objectMapper.writeValueAsString(request);
    }
}