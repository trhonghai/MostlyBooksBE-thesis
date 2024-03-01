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


    private final BookRepository bookRepository;

    private final OrderDetailRepository  orderDetailRepository;


    @GetMapping
    public ResponseEntity<List<OrderDetail>> getCart(@RequestParam("cart") Long cartId){
        List<OrderDetail> orderDetails = cartRepository.findById(cartId).orElseThrow().getOrderDetail();
        return ResponseEntity.ok().body(orderDetails);
    }


    @PostMapping("/add-to-cart")
    public ResponseEntity<List<OrderDetail>> addToCart(@RequestBody CartItem cartItem){
        Cart cart = cartRepository.findById((long) cartItem.getCartId()).orElseThrow(()->new NotFoundException("cart " +
                "not found, " +
                "id="+cartItem.getCartId()));
        Book book = bookRepository.findById(cartItem.getBookId()).get();
        List<OrderDetail> orderDetails = cart.getOrderDetail();
        for (OrderDetail orderDetail : orderDetails) {
            if (orderDetail.getBook().getId().equals((long) cartItem.getBookId())) {
                orderDetail.setQuantity(orderDetail.getQuantity() + cartItem.getQuantity());
                cartRepository.save(cart);
                return ResponseEntity.ok(orderDetails);
            }
        }
        OrderDetail newOrderDetail = new OrderDetail();
        orderDetails.add(newOrderDetail);
        newOrderDetail.setBook(book);
        newOrderDetail.setPrice(cartItem.getPrice());
        newOrderDetail.setQuantity(cartItem.getQuantity());
        newOrderDetail.setCart(cart);
        orderDetailRepository.save(newOrderDetail);
        cartRepository.save(cart);
        return ResponseEntity.ok().body(orderDetails);
    }


    @DeleteMapping("/delete-item")
    public ResponseEntity<List<OrderDetail>> removeFromCart(@RequestParam("cart") Long cartId,
                                                            @RequestParam("item") Long orderDetailsId ){
        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new NotFoundException("cart " +
                "not found, " +
                "id="+cartId));
        List<OrderDetail> orderDetails =cart.getOrderDetail();
        for (OrderDetail o:
                orderDetails) {
            if(o.getId().equals(orderDetailsId)){
                System.out.println("o="+o.getId()+"id="+orderDetailsId);
                cart.removeOrderDetails(o);
                break;
            }
        }
        cartRepository.save(cart);
        List<OrderDetail> newOrderDetails = cart.getOrderDetail().stream().toList();


        return ResponseEntity.ok(newOrderDetails);
    }
    @PutMapping("/update-quantity")
    public ResponseEntity<List<OrderDetail>> updateItemQuantity(@RequestParam("cart") Long cartId ,
                                                                @RequestParam("item") Long orderDetailsId,
                                                                @RequestParam("method") String method){
        Cart cart = cartRepository.findById(cartId).orElseThrow( ()-> new NotFoundException("cart " +
                "not found, " +
                "id="+cartId));
        List<OrderDetail> orderDetails =cart.getOrderDetail();
        for (OrderDetail o:
                orderDetails) {
            if(o.getId().equals(Long.valueOf(orderDetailsId))){
                if(method.equals("increase")){
                    System.out.println("o="+o.getId()+"id="+orderDetailsId);
                    o.setQuantity(o.getQuantity()+1);
                }else if(method.equals("decrease")){
                    if(o.getQuantity()==1){
                        cart.removeOrderDetails(o);
                    }else{
                        o.setQuantity(o.getQuantity()-1);
                    }
                }
                break;
            }
        }
        cartRepository.save(cart);
        List<OrderDetail> newOrderDetails = cart.getOrderDetail().stream().toList();
        return ResponseEntity.ok(newOrderDetails);
    }





}
