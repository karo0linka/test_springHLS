package com.hls.springboot_app.repository;
import com.hls.springboot_app.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindAllUsers() {
        User user = new User("Luis", "luis@hls.com");
        userRepository.save(user);

        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
    }
}
