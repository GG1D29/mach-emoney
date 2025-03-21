package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.UnauthorizedException;
import com.stanley.xie.emoney.exception.UsernameNotFoundException;
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
        Mockito.when(userRepository.existsByUsername("username")).thenReturn(true);
        boolean actual = userService.isExist("username");
        assertThat(actual).isTrue();
    }

    @Test
    void should_False_When_Call_IsExist() {
        Mockito.when(userRepository.existsByUsername("username")).thenReturn(false);
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
    void should_FindUserByToken_Successfully() {
        User expected = new User("username", "token");
        Mockito.when(userRepository.findByUserToken("token")).thenReturn(Optional.of(expected));

        User actual = userService.getUserByToken("token");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_Failed_FindUserByToken_When_InvalidToken() {
        Mockito.when(userRepository.findByUserToken("token")).thenReturn(Optional.empty());

        assertThrows(UnauthorizedException.class, () -> userService.getUserByToken("token"));
    }

    @Test
    void should_UpdateUser_Successfully() {
        User expected = new User("username", "token");
        userService.updateUser(expected);

        Mockito.verify(userRepository).save(expected);
    }

    @Test
    void should_FindUserByName_Successfully() {
        User expected = new User("username", "token");
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.of(expected));

        User actual = userService.getUserByUsername("username");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void should_Failed_FindUserByName_When_UsernameNotFound() {
        Mockito.when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.getUserByUsername("username"));
    }
}