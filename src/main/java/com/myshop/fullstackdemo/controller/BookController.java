package com.myshop.fullstackdemo.controller;

import com.myshop.fullstackdemo.model.Book;
import com.myshop.fullstackdemo.model.DetailsImage;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.service.BookService;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

import java.util.List;


@Controller
@RequiredArgsConstructor
@CrossOrigin("http://localhost:3000")
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;


    @GetMapping("/books")
    ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.listAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Book> getBookById(@PathVariable Long id){
        Book book = bookService.getABook(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("books/new")
    public ResponseEntity<Book> createBook(@RequestBody Book book,  @RequestPart("image") MultipartFile multipartFile) throws IOException, StripeException {
        System.out.println("Book Authour"+ book.getAuthour().getId());
        Book createdBook = bookService.saveBook(book, multipartFile);

        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }

    @PostMapping("books/detailImages/{id}")
    public ResponseEntity<DetailsImage> createImagesBook(@PathVariable Long id,@RequestBody DetailsImage detailsImageRe ){
        Book book = bookRepository.findById(id).get();
        DetailsImage detailsImage = new DetailsImage();
        detailsImage.setImage(detailsImageRe.getImage());
        detailsImage.setBook(book);
        List<DetailsImage> detailsImages = new ArrayList<>();
        detailsImages.add(detailsImage);
        book.setImages(detailsImages);
        Book savedBook = bookRepository.save(book);
        return ResponseEntity.ok(detailsImage);
    }


}
