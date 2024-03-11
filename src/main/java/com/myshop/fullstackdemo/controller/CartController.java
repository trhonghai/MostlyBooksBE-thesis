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

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @PostMapping("/checkout-items")
    public ResponseEntity<List<Long>> deleteCheckoutListFromCart(@RequestParam("cart") Long cartId,
                                                                 @RequestBody List<Long> orderDetailIds) {
        System.out.println("OrderDetail IDs: " + orderDetailIds);
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new NotFoundException("Cart not found, id=" + cartId));

        List<OrderDetail> deletedOrderDetails = new ArrayList<>();
        for (Long orderDetailId : orderDetailIds) {
            OrderDetail orderDetail = orderDetailRepository.findById(orderDetailId)
                    .orElseThrow(() -> new NotFoundException("OrderDetail not found, id=" + orderDetailId));
            cart.removeOrderDetails(orderDetail);
            deletedOrderDetails.add(orderDetail);
        }
        cartRepository.save(cart);

        // Trả về danh sách các ID của các OrderDetail đã bị xóa
        List<Long> deletedOrderDetailIds = deletedOrderDetails.stream()
                .map(OrderDetail::getId)
                .collect(Collectors.toList());

        return ResponseEntity.ok().body(deletedOrderDetailIds);
    }
//    public ResponseEntity<List<OrderDetail>> DeleteCheckoutListFromCart(@RequestParam("cart") Long cartId,
//                                                                        @RequestBody List<OrderDetail> OrderDetailsId){
//        System.out.println("cartList = " + OrderDetailsId);
//        Cart cart = cartRepository.findById(cartId).orElseThrow(()-> new NotFoundException("cart " +
//                "not found, " +
//                "id="+cartId));
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        OrderDetailsId.forEach(cartItem -> {
//            OrderDetail orderDetail = orderDetailRepository.findById(cartItem.getId()).orElseThrow(() -> new NotFoundException("OrderDetail " +
//                    "not found, " +
//                    "id=" + cartItem.getId()));
//            cart.removeOrderDetails(orderDetail);
//            orderDetails.add(orderDetail);
//        });
//        cartRepository.save(cart);
//        return ResponseEntity.ok().body(orderDetails);
//    }
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

//    @PostMapping("/checkout-items")
//    public ResponseEntity<List<OrderDetail>> getAList(@RequestBody List<OrderDetail> OrderDetailsId){
//        System.out.println("cartList = " + OrderDetailsId);
//        List<OrderDetail> orderDetails = new ArrayList<>();
//        OrderDetailsId.forEach(cartItem -> {
//           OrderDetail orderDetail = orderDetailRepository.findById(cartItem.getCartId()).orElseThrow(() -> new NotFoundException("OrderDetail " +
//                   "not found, " +
//                   "id=" + cartItem.getCartId()));
//           orderDetail.getCart().removeOrderDetails(orderDetail);
//           orderDetails.add(orderDetail);
//        });
//        return ResponseEntity.ok().body(orderDetails);
//    }



}
