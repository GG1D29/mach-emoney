package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InsufficientBalanceException;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.exception.UsernameNotFoundException;
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
public class MoneyTransferServiceIT {
    @Autowired
    private MoneyTransferService moneyTransferService;

    @Autowired
    private UserBalanceService userBalanceService;

    @Autowired
    private UserRegistrationService userRegistrationService;

    @Test
    void should_TransferMoney_Successfully() {
        // 1. First create two users to simulate the money transfer.
        String token = userRegistrationService.register("shenli");
        int balance = userBalanceService.fetchBalance(token);
        assertThat(balance).isZero();

        String token2 = userRegistrationService.register("hartono");
        int balance2 = userBalanceService.fetchBalance(token2);
        assertThat(balance2).isZero();

        // 2. Top up first account with some amount.
        userBalanceService.topUp(token, 25);

        // 3. Perform transfer from shenli to hartono.
        moneyTransferService.transfer(token, "hartono", 15);

        // 4. Check balance is correct.
        balance = userBalanceService.fetchBalance(token);
        assertThat(balance).isEqualTo(10);

        balance2 = userBalanceService.fetchBalance(token2);
        assertThat(balance2).isEqualTo(15);
    }

    @Test
    void should_Failed_TransferMoney_When_NoBalance() {
        String token = userRegistrationService.register("shenli");
        userRegistrationService.register("hartono");

        assertThrows(InsufficientBalanceException.class,
                () -> moneyTransferService.transfer(token, "hartono", 15));
    }

    @Test
    void should_Failed_TransferMoney_When_Unauthorized() {
        assertThrows(UnauthorizedException.class,
                () -> moneyTransferService.transfer("token", "hartono", 15));
    }

    @Test
    void should_Failed_TransferMoney_When_TargetUsernameNotFound() {
        String token = userRegistrationService.register("shenli");
        userBalanceService.topUp(token, 25);

        assertThrows(UsernameNotFoundException.class,
                () -> moneyTransferService.transfer(token, "hartono", 15));

    }
}
