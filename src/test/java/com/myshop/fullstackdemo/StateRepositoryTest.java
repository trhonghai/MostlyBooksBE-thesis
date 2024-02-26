package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Country;
import com.myshop.fullstackdemo.model.State;
import com.myshop.fullstackdemo.repository.CountryRepository;
import com.myshop.fullstackdemo.repository.StateRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class StateRepositoryTest {

    @Autowired
    private StateRepository stateRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testCreateState(){
        long countryId = 1;
        Country country = countryRepository.findById(countryId).get();
        State state = new State();
        state.setName("Cần Thơ");
        state.setCountry(country);
        State savedState = stateRepository.save(state);
        Assertions.assertThat(savedState.getId()).isGreaterThan(0);

    }
}
