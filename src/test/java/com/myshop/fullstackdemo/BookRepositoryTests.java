package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.*;
import com.myshop.fullstackdemo.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class BookRepositoryTests {
    @Autowired
    private  BookRepository  bookRepository;
    @Autowired
    private  TestEntityManager entityManager;

    @Test
    public void testCreateProduct(){
        Category category = entityManager.find(Category.class,1);
        Publisher publisher = entityManager.find(Publisher.class,1);
        Authour authour = entityManager.find(Authour.class,1);
        Book book = new Book();
        book.setName("Cây cam ngọt của tôi");
        book.setDescription("Mở đầu bằng những thanh âm trong sáng và kết thúc lắng lại trong những nốt trầm hoài niệm");
        book.setCategory(category);
        book.setPublisher(publisher);
        book.setAuthour(authour);
        book.setPrice(89000);
        book.setImg("https://cdn0.fahasa.com/media/catalog/product/i/m/image_217480.jpg");
        book.setPages(244L);
        book.setInventory(23L);
        book.setRating(4.5f);
        Book savedBook = bookRepository.save(book);
        Assertions.assertThat(savedBook.getId()).isGreaterThan(0);
    }

    @Test
    public void testSaveBookWithImage(){
        long bookId = 1;
        Book book = bookRepository.findById(bookId).get();

         DetailsImage detailsImage = new DetailsImage();
        detailsImage.setImage("https://khoiphucvn.com/storage/sach-trong-nuoc/ccnct-4.jpg");

        // Gán cuốn sách cho chi tiết hình ảnh
        detailsImage.setBook(book);

        // Tạo một danh sách mới chứa chi tiết hình ảnh
        List<DetailsImage> detailsImages = new ArrayList<>();
        detailsImages.add(detailsImage);

        // Gán danh sách chi tiết hình ảnh vào cuốn sách
        book.setImages(detailsImages);

        // Lưu cuốn sách vào cơ sở dữ liệu
        Book savedBook = bookRepository.save(book);


        Assertions.assertThat(savedBook.getImages()).size().isEqualTo(1);
    }
}
