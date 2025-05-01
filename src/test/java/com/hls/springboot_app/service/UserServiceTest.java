package com.hls.springboot_app.service;

import com.hls.springboot_app.model.User;
import com.hls.springboot_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void shouldSaveUser() {
        User user = new User("Luis", "luis@hls.com");
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.saveUser(user);

        assertEquals("Luis", result.getName());
        assertEquals("luis@hls.com", result.getEmail());
    }
}
