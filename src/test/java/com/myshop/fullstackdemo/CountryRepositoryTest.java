package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Country;
import com.myshop.fullstackdemo.repository.CountryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testCreateCountry(){
        Country country = new Country();
        country.setName("Việt Nam");
        country.setCode("VN");
        Country savedCountry = countryRepository.save(country);
        Assertions.assertThat(savedCountry.getId()).isGreaterThan(0);
    }

}
