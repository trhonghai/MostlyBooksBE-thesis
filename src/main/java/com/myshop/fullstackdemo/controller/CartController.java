package com.myshop.fullstackdemo.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.myshop.fullstackdemo.dto.CartItem;
import com.myshop.fullstackdemo.exception.NotFoundException;
import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.Cart;
import com.myshop.fullstackdemo.model.OrderDetail;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.CartRepository;
import com.myshop.fullstackdemo.repository.OrderDetailRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("user-cart")
@RequiredArgsConstructor

public class CartController {
    private final CartRepository cartRepository;
    @PersistenceContext
    private EntityManager entityManager;

    private final BookRepository bookRepository;

    private final OrderDetailRepository  orderDetailRepository;


    @GetMapping
    public ResponseEntity<List<OrderDetail>> getCart(@RequestParam("cart") Long cartId){
        List<OrderDetail> orderDetails = cartRepository.findById(cartId).orElseThrow().getOrderDetail();
        return ResponseEntity.ok(orderDetails);
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<OrderDetail> addToCart(@RequestBody CartItem cartItem){
        Cart cart = cartRepository.findById((long) cartItem.getCartId()).orElseThrow(()->new NotFoundException("cart " +
                "not found, " +
                "id="+cartItem.getCartId()));
        Book book = entityManager.getReference(Book.class, cartItem.getBookId());
        List<OrderDetail> orderDetails = cart.getOrderDetail();
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getBook().getId().equals((long) cartItem.getBookId())) {
                orderDetail.setQuantity(orderDetail.getQuantity() + 1);
                cartRepository.save(cart);
                return ResponseEntity.ok(orderDetail);
            }
        }
        OrderDetail newOrderDetail = new OrderDetail();
        newOrderDetail.setBook(book);
        newOrderDetail.setPrice(cartItem.getPrice());
        newOrderDetail.setQuantity(cartItem.getQuantity());
        newOrderDetail.setCart(cart);
        orderDetailRepository.save(newOrderDetail);
        orderDetails.add(newOrderDetail);
        cartRepository.save(cart);
        return ResponseEntity.ok(newOrderDetail);
    }





//    @PutMapping("/update-quantity")
//    public ResponseEntity<List<OrderDetail>> updateItemQuantity(@RequestParam("cart") String cartId)




}
