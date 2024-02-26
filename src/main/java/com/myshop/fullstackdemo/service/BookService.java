package com.myshop.fullstackdemo.service;

import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        public Book saveBook(Book book){
            Authour authour = authourRepository.findById(Math.toIntExact(book.getAuthour().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid author Id"));
            Category category = categoryRepository.findById(Math.toIntExact(book.getCategory().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid category Id"));
            Publisher publisher = publisherRepository.findById(Math.toIntExact(book.getPublisher().getId())).orElseThrow(() -> new IllegalArgumentException("Invalid publisher Id"));
            book.setAuthour(authour);
            book.setCategory(category);
            book.setPublisher(publisher);
            return bookRepository.save(book);
        }

        public Book getABook(Long id){
            return bookRepository.findById(id).get();
        }

        public DetailsImage createDetailImage(DetailsImage detailsImage){
            return detailsImageRepository.save(detailsImage);
        }



}
