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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;
    private final BookRepository bookRepository;
    private final AuthourRepository authourRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;


    @GetMapping
    ResponseEntity<List<Book>> getAllBooks(){
        return new ResponseEntity<>(bookService.listAllBooks(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    ResponseEntity<Book> getBookById(@PathVariable Long id){
        Book book = bookService.getABook(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }

    @PostMapping("/new")
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

    @PostMapping("/detailImages/{id}")
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

    @PutMapping("/update/{id}")
    public ResponseEntity<Book> updateBook(
            @PathVariable Long id,
            @RequestParam String bookUpdate,
            @RequestPart(value = "image" , required = false) MultipartFile multipartFile
    ) throws IOException, StripeException {
        ObjectMapper objectMapper = new ObjectMapper();
        Book updatedBook = objectMapper.readValue(bookUpdate, Book.class);

        // Tìm sách cần cập nhật trong cơ sở dữ liệu
        return bookRepository.findById(id)
                .map(existingBook -> {
                    // Cập nhật thông tin sách
                    existingBook.setName(updatedBook.getName());
                    existingBook.setDescription(updatedBook.getDescription());
                    existingBook.setPrice(updatedBook.getPrice());
                    existingBook.setPages(updatedBook.getPages());
                    existingBook.setISBN_10(updatedBook.getISBN_10());
                    existingBook.setISBN_13(updatedBook.getISBN_13());
                    existingBook.setDimensions(updatedBook.getDimensions());
                    existingBook.setIssue(updatedBook.getIssue());
                    existingBook.setCover(updatedBook.getCover());
                    existingBook.setWeight(updatedBook.getWeight());
                    existingBook.setRating(updatedBook.getRating());
                    existingBook.setReviewCount(updatedBook.getReviewCount());
                    existingBook.setInventory(updatedBook.getInventory());
                    // Cập nhật hình ảnh nếu có
                    if (multipartFile != null) {
                        Stripe.apiKey = "sk_test_51OU2F8ImzaickdDEJwnDISY6ToIMgENs2mySnK4umkviZ3CbqMtoGDf4oQrlz14Id1N0oxXCnXwUBN6L1jQmNJXM00TILxNv2r";
                        byte[] img_data;
                        try {
                            img_data = multipartFile.getBytes();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        String currentWorkingDirectory = System.getProperty("user.dir");
                        System.out.println("name=" + multipartFile.getOriginalFilename());
                        String filePath = currentWorkingDirectory + "/" + multipartFile.getOriginalFilename();
                        java.io.File fileNew = new java.io.File(filePath);
                        java.io.FileOutputStream fileOutputStream;
                        try {
                            fileOutputStream = new java.io.FileOutputStream(filePath);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            fileOutputStream.write(img_data);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        try {
                            fileOutputStream.close();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                        com.stripe.param.FileCreateParams fileCreateParams = com.stripe.param.FileCreateParams.builder()
                                .setFile(fileNew)
                                .setPurpose(com.stripe.param.FileCreateParams.Purpose.DISPUTE_EVIDENCE)
                                .build();
                        File file = null;
                        try {
                            file = File.create(fileCreateParams);
                        } catch (StripeException e) {
                            throw new RuntimeException(e);
                        }

                        FileLinkCreateParams params =
                                FileLinkCreateParams.builder()
                                        .setFile(file.getId())
                                        .build();
                        FileLink fileLink;
                        try {
                            fileLink = FileLink.create(params);
                        } catch (StripeException e) {
                            throw new RuntimeException(e);
                        }

                        fileNew.delete();
                        existingBook.setImg(fileLink.getUrl());
                    }

                    // Lưu và trả về sách đã cập nhật
                    return ResponseEntity.ok(bookRepository.save(existingBook));
                })
                .orElse(ResponseEntity.notFound().build()); // Trả về 404 nếu không tìm thấy sách
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Book>> filterBook(
            @RequestParam(value = "minPrice",required = false) Float minPrice,
            @RequestParam(value = "maxPrice",required = false) Float maxPrice,
            @RequestParam(value = "categoryName",required = false) String categoryName,
            @RequestParam(value="publisherName",required = false) String publisherName,
            @RequestParam(value="coverType",required = false) String cover
    ) {
        List<Book> filteredBooks = bookService.filterBooks(minPrice, maxPrice, categoryName, publisherName, cover);
        return  ResponseEntity.ok(filteredBooks);
    }
}
