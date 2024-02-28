package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Country;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.repository.CountryRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CustomerRepositoryTests {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CountryRepository countryRepository;

    @Test
    public void testCreateCustomer(){
        long countryId = 1;
        Country country = countryRepository.findById(countryId).get();
        Customer customer = new Customer();
//        customer.setCountry(country);
        customer.setFirstName("Truong");
        customer.setLastName("Hong Hai");
        customer.setPassword("35115442");
        customer.setEmail("trhonghaii@gmail.com");
        customer.setPhone("0935115442");
//        customer.setAddress_line("B4 KDC91B");
//        customer.setCity("Cần Thơ");
//        customer.setState("Ninh kiều");

        Customer savedCustomer = customerRepository.save(customer);
        Assertions.assertThat(savedCustomer.getId()).isGreaterThan(0);

    }
}
