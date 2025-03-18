package com.stanley.xie.emoney.service;

import com.stanley.xie.emoney.exception.DuplicateUsernameException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {
    @InjectMocks
    private UserRegistrationService userRegistrationService;

    @Mock
    private UserService userService;

    @Test
    void should_RegisterNewUser_Successfully() {
        String token = userRegistrationService.register("username");
        assertThat(token).isNotEmpty();

        Mockito.verify(userService).saveUser(eq("username"), anyString());
    }

    @Test
    void should_Failed_RegisterNewUser_When_UsernameAlreadyExists() {
        Mockito.when(userService.isExist("username")).thenReturn(true);
        assertThrows(DuplicateUsernameException.class, () -> userRegistrationService.register("username"));

        Mockito.verify(userService, never()).saveUser(eq("username"), anyString());
    }

}