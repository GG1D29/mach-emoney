package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.UnauthorizedException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Transactional
public class UserBalanceServiceIT {
    @Autowired
    private UserRegistrationService userRegistrationService;

    @Autowired
    private UserBalanceService userBalanceService;

    @Test
    void should_TopUpBalance_Successfully() {
        // 1. First create new user
        String token = userRegistrationService.register("shenli");

        // 2. Fetch the balance for first time, it will return empty balance
        int balance = userBalanceService.fetchBalance(token);
        assertThat(balance).isEqualTo(0);

        // 3. Try top up some amount, make sure the balance increased.
        userBalanceService.topUp(token, 10);
        balance = userBalanceService.fetchBalance(token);
        assertThat(balance).isEqualTo(10);

        // 4. Top up more balance.
        userBalanceService.topUp(token, 15);
        balance = userBalanceService.fetchBalance(token);
        assertThat(balance).isEqualTo(25);
    }

    @Test
    void should_Failed_TopUpBalance_When_TokenNotFound() {
        assertThrows(UnauthorizedException.class, () -> userBalanceService.topUp("shenli", 10));
    }

    @Test
    void should_Failed_FetchBalance_When_TokenNotFound() {
        assertThrows(UnauthorizedException.class, () -> userBalanceService.fetchBalance("shenli"));
    }
}
