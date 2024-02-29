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
        publisher.setName("Nhà xuất bản Hội nhà văn");
        publisher.setEmail(" nhaxuatbanhnv65@gmail.com");
        publisher.setAddress("65, Nguyễn Du, quận Hai Bà Trưng, Hà Nội");
        publisher.setPhone("024 38222135");
        publisher.setPhotos("https://www.netabooks.vn/data/author/18246/logo--nxb-hoi-nha-van.jpg");
        Publisher savedPublisher=repo.save(publisher);
        Assertions.assertThat(savedPublisher.getId()).isGreaterThan(0);
    }

}
