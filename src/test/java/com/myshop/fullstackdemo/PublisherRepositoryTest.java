package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Category;
import com.myshop.fullstackdemo.model.Publisher;
import com.myshop.fullstackdemo.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.annotation.Rollback;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)

public class PublisherRepositoryTest {
    @Autowired
    private  PublisherRepository repo;

    @Test
    public void testCreatePublisher(){
        Publisher publisher = new Publisher();
        publisher.setName("Nhà xuất bản phụ nữ Việt Nam");
        publisher.setEmail("truyenthongvaprnxbpn@gmail.com");
        publisher.setAddress("39 P. Hàng Chuối, Phạm Đình Hổ, Hai Bà Trưng, Hà Nội");
        publisher.setPhone("024 3971 0717");
        publisher.setPhotos("https://www.netabooks.vn/data/author/16073/logo-nxb-phu-nu.jpg");
        Publisher savedPublisher=repo.save(publisher);
        Assertions.assertThat(savedPublisher.getId()).isGreaterThan(0);
    }

}
