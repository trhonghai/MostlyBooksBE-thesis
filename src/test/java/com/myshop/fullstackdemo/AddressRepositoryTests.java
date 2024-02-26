package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Address;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.repository.AddressRepository;
import com.myshop.fullstackdemo.repository.CountryRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class AddressRepositoryTests {
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Test
    public void testCreateAddress(){
        long customerId = 1;
        Customer customer = customerRepository.findById(customerId).get();
        long countryId = 1;
        Address address = new Address();
        address.setCustomer(customer);
        address.setCountry(countryRepository.findById(countryId).get());
        address.setFirstName("Truong");
        address.setLastName("Hong Hai");
        address.setPhoneNumber("0935115442");
        address.setAddress("B4 KDC91B");
        address.setCity("Cần Thơ");
        address.setState("Ninh Kiều");
        Address savedAddress = addressRepository.save(address);
        Assertions.assertThat(savedAddress.getId()).isGreaterThan(0);


    }

}
