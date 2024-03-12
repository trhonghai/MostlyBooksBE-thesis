package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.*;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.File;
import com.stripe.model.FileLink;
import com.stripe.param.FileCreateParams;
import com.stripe.param.FileLinkCreateParams;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthourRepository authourRepository;
    private final CategoryRepository categoryRepository;
    private final PublisherRepository publisherRepository;
    private final DetailsImageRepository detailsImageRepository;
    public List<Book> listAllBooks(){
        return (List<Book>) bookRepository.findAll();
    }

        public Book saveBook(Book book, MultipartFile multipartFile) throws IOException, StripeException {
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

            Authour authour = authourRepository.findById(Math.toIntExact(book.getAuthour().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid author Id"));
            Category category = categoryRepository.findById(Math.toIntExact(book.getCategory().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
            Publisher publisher = publisherRepository.findById(Math.toIntExact(book.getPublisher().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid publisher Id"));
            book.setAuthour(authour);
            book.setCategory(category);
            book.setPublisher(publisher);
            book.setImg(fileLink.getUrl());

            return bookRepository.save(book);
        }

        public Book getABook(Long id){
            return bookRepository.findById(id).get();
        }

        public DetailsImage createDetailImage(DetailsImage detailsImage){
            return detailsImageRepository.save(detailsImage);
        }



}
