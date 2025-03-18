package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.InvalidTopUpAmountException;
import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class BalanceTopUpServiceTest {
    @InjectMocks
    private BalanceTopUpService service;

    @Mock
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void should_TopUpBalance_Successfully() {
        User user = new User();
        user.setBalance(150);
        Mockito.when(userService.getUserByToken("userToken")).thenReturn(user);

        service.topUp("userToken", 100);

        Mockito.verify(userService).updateUser(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.getBalance()).isEqualTo(250);
    }

    @Test
    void should_Failed_TopUpBalance_When_ZeroAmount() {
        assertThrows(InvalidTopUpAmountException.class, () -> service.topUp("userToken", 0));
        Mockito.verify(userService, never()).updateUser(any(User.class));
    }

    @Test
    void should_Failed_TopUpBalance_When_NegativeAmount() {
        assertThrows(InvalidTopUpAmountException.class, () -> service.topUp("userToken", -100));
        Mockito.verify(userService, never()).updateUser(any(User.class));
    }

    @Test
    void should_Failed_TopUpBalance_When_ExceedingMaximumAmount() {
        assertThrows(InvalidTopUpAmountException.class, () -> service.topUp("userToken", 10000001));
        Mockito.verify(userService, never()).updateUser(any(User.class));
    }

    @Test
    void should_Failed_TopUpBalance_When_Unauthorized() {
        Mockito.when(userService.getUserByToken("userToken")).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> service.topUp("userToken", 1000));
    }
}