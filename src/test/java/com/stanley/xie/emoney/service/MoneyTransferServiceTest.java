package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InsufficientBalanceException;
import com.stanley.xie.emoney.exception.InvalidTransferAmountException;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.exception.UsernameNotFoundException;
import com.stanley.xie.emoney.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class MoneyTransferServiceTest {
    @InjectMocks
    private TheMoneyTransferService service;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void should_TransferMoney_Successfully() {
        User fromUser = getUserWithBalance();
        User toUser = getUserWithBalance();

        Mockito.when(userService.getUserByToken("userToken")).thenReturn(fromUser);
        Mockito.when(userService.getUserByUsername("targetUser")).thenReturn(toUser);

        service.transfer("userToken", "targetUser", 5);

        Mockito.verify(userService, times(2)).updateUser(userArgumentCaptor.capture());
        List<User> updatedUsers = userArgumentCaptor.getAllValues();
        User user1 = updatedUsers.get(0);
        assertThat(user1.getBalance()).isEqualTo(5);

        User user2 = updatedUsers.get(1);
        assertThat(user2.getBalance()).isEqualTo(15);
    }

    @Test
    void should_Failed_TransferMoney_When_ZeroAmount() {
        User fromUser = getUserWithBalance();
        Mockito.when(userService.getUserByToken("userToken")).thenReturn(fromUser);
        assertThrows(InvalidTransferAmountException.class,
                () -> service.transfer("userToken", "targetUser", 0));
    }

    @Test
    void should_Failed_TransferMoney_When_NegativeAmount() {
        User fromUser = getUserWithBalance();
        Mockito.when(userService.getUserByToken("userToken")).thenReturn(fromUser);
        assertThrows(InvalidTransferAmountException.class,
                () -> service.transfer("userToken", "targetUser", -10));
    }

    @Test
    void should_Failed_TransferMoney_When_NotEnoughBalance() {
        User fromUser = getUserWithBalance();

        Mockito.when(userService.getUserByToken("userToken")).thenReturn(fromUser);

        assertThrows(InsufficientBalanceException.class,
                () -> service.transfer("userToken", "targetUser", 11));
    }

    @Test
    void should_Failed_TransferMoney_When_Unauthorized() {
        Mockito.when(userService.getUserByToken("userToken")).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class,
                () -> service.transfer("userToken", "targetUser", 5));
    }

    @Test
    void should_Failed_TransferMoney_When_TargetUsernameNotFound() {
        User fromUser = getUserWithBalance();

        Mockito.when(userService.getUserByToken("userToken")).thenReturn(fromUser);
        Mockito.when(userService.getUserByUsername("targetUser")).thenThrow(UsernameNotFoundException.class);

        assertThrows(UsernameNotFoundException.class,
                () -> service.transfer("userToken", "targetUser", -5));
    }

    private User getUserWithBalance() {
        User user = new User();
        user.setBalance(10);

        return user;
    }
}