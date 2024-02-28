package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Cart;
import com.myshop.fullstackdemo.model.Customer;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.CartRepository;
import com.myshop.fullstackdemo.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CartItemRepositoryTests {

    @Autowired
    private CartRepository cartItemRepository;
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CustomerRepository customerRepository;
//    @Test
//    public void testSaveItem(){
//        long customerId = 1;
//        long bookId =1 ;
//        Book book = bookRepository.findById(bookId).get();
//        Customer customer = customerRepository.findById(customerId).get();
//        Cart newItem = new Cart();
//        newItem.setBook(book);
//        newItem.setCustomer(customer);
//        newItem.setQuantity(1);
//        cartItemRepository.save(newItem);
//    }
}
