package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @InjectMocks
    private TheUserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Test
    void should_True_When_Call_IsExist() {
        Mockito.when(userRepository.isExistsByUsername("username")).thenReturn(true);
        boolean actual = userService.isExist("username");
        assertThat(actual).isTrue();
    }

    @Test
    void should_False_When_Call_IsExist() {
        Mockito.when(userRepository.isExistsByUsername("username")).thenReturn(false);
        boolean actual = userService.isExist("username");
        assertThat(actual).isFalse();
    }

    @Test
    void should_createUser_Successfully() {
        userService.createUser("username", "token");

        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("username");
        assertThat(savedUser.getUserToken()).isEqualTo("token");
        assertThat(savedUser.getBalance()).isZero();
    }

    @Test
    void should_ReturnUser_When_ValidToken() {
        User expected = new User("username", "token");
        Mockito.when(userRepository.findByUserToken("token")).thenReturn(Optional.of(expected));

        User actual = userService.getUserByToken("token");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_ThrowException_When_InvalidToken() {
        Mockito.when(userRepository.findByUserToken("token")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.getUserByToken("token"));
    }
}