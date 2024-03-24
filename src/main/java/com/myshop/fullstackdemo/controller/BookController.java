package com.myshop.fullstackdemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.AuthourRepository;
import com.myshop.fullstackdemo.repository.BookRepository;
import com.myshop.fullstackdemo.repository.CategoryRepository;
import com.myshop.fullstackdemo.repository.PublisherRepository;
import com.myshop.fullstackdemo.service.BookService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.File;
import com.stripe.model.FileLink;
import com.stripe.param.FileLinkCreateParams;
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

public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AuthourRepository authourRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;


    @GetMapping("/books")
    ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.listAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/books/{id}")
    ResponseEntity<Book> getBookById(@PathVariable Long id){
        Book book = bookService.getABook(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/books/new")
    public ResponseEntity <Book> createBook(@RequestParam String book, @RequestPart("image") MultipartFile multipartFile) throws IOException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("book" + book);
        Book bookEntity = objectMapper.readValue(book, Book.class);


        Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";
        byte[] img_data;
        img_data = multipartFile.getBytes();
        String currentWorkingDirectory = System.getProperty("user.dir");
        System.out.println("name="+multipartFile.getOriginalFilename());
        String filePath = currentWorkingDirectory + "/"+multipartFile.getOriginalFilename();
        java.io.File fileNew = new java.io.File(filePath);
        java.io.FileOutputStream fileOutputStream;
        fileOutputStream = new java.io.FileOutputStream(filePath);
        fileOutputStream.write(img_data);
        fileOutputStream.close();

        com.stripe.param.FileCreateParams fileCreateParams = com.stripe.param.FileCreateParams.builder()
                .setFile(fileNew)
                .setPurpose(com.stripe.param.FileCreateParams.Purpose.DISPUTE_EVIDENCE)
                .build();
        File file = File.create(fileCreateParams);

        FileLinkCreateParams params =
                FileLinkCreateParams.builder()
                        .setFile(file.getId())
                        .build();
        FileLink fileLink = FileLink.create(params);

        fileNew.delete();

        Authour authour = authourRepository.findById(Math.toIntExact(bookEntity.getAuthour().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid author Id"));
        Category category = categoryRepository.findById(Math.toIntExact(bookEntity.getCategory().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
        Publisher publisher = publisherRepository.findById(Math.toIntExact(bookEntity.getPublisher().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid publisher Id"));

        bookEntity.setAuthour(authour);
        bookEntity.setCategory(category);
        bookEntity.setPublisher(publisher);
        bookEntity.setImg(fileLink.getUrl());
        bookEntity.setIssue(bookEntity.getIssue());
        bookEntity.setPages(bookEntity.getPages());
        bookEntity.setPrice(bookEntity.getPrice());
        bookEntity.setISBN_10(bookEntity.getISBN_10());
        bookEntity.setISBN_13(bookEntity.getISBN_13());
        bookEntity.setDimensions(bookEntity.getDimensions());
        bookEntity.setCover(bookEntity.getCover());
        bookEntity.setWeight(bookEntity.getWeight());
        bookEntity.setRating(bookEntity.getRating());
        bookEntity.setInventory(bookEntity.getInventory());
        bookRepository.save(bookEntity);

//        return bookEntity;
        return new ResponseEntity<>(bookEntity, HttpStatus.CREATED);
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
