package com.hls.springboot_app.controller;

import com.hls.springboot_app.model.User;
import com.hls.springboot_app.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void shouldCreateUser() throws Exception {
        User user = new User("Luis", "bribiesca.luis@hlsgroup.com.mx");

        when(userRepository.save(any(User.class))).thenReturn(user);

        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Luis\",\"email\":\"bribiesca.luis@hlsgroup.com.mx\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Luis"))
                .andExpect(jsonPath("$.email").value("bribiesca.luis@hlsgroup.com.mx"));
    }
}
