package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class BalanceFetchServiceTest {
    @InjectMocks
    private BalanceFetchService service;

    @Mock
    private UserService userService;

    @Test
    void should_FetchUserBalance_Successfully() {
        User user = new User();
        user.setBalance(150);

        Mockito.when(userService.getUserByToken("userToken")).thenReturn(user);
        int balance = service.fetchBalance("userToken");
        assertThat(balance).isEqualTo(150);
    }

    @Test
    void should_Failed_FetchUserBalance_When_Unauthorized() {
        Mockito.when(userService.getUserByToken("userToken")).thenThrow(UnauthorizedException.class);

        assertThrows(UnauthorizedException.class, () -> service.fetchBalance("userToken"));
    }
}