package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Authour;
import com.myshop.fullstackdemo.repository.AuthourRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AuthourRepositoryTests {
    @Autowired
    private AuthourRepository authourRepository;

    @Test
    public void testCreateAuthour(){
        Authour authour = new Authour();
        authour.setName("José Mauro de Vasconcelos");
        Authour savedAuthour = authourRepository.save(authour);
        Assertions.assertThat(savedAuthour.getId()).isGreaterThan(0);
    }
}
