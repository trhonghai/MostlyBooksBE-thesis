package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.User;
import com.myshop.fullstackdemo.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
    @Autowired
    private UserRepository repo;
    @Test
    public void testGetUserByEmail() {
        String email = "admin@gmail.com";
        User user = repo.findByEmail(email).orElseThrow();
        assertThat(user).isNotNull();

    }
}
