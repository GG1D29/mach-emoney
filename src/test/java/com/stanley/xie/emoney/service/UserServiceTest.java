package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.model.User;
import com.stanley.xie.emoney.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

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
    void should_SaveUser_Successfully() {
        userService.saveUser("username", "token");

        Mockito.verify(userRepository).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.getUsername()).isEqualTo("username");
        assertThat(savedUser.getUserToken()).isEqualTo("token");
        assertThat(savedUser.getBalance()).isZero();
    }
}