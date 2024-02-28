package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Role;
import com.myshop.fullstackdemo.repository.RoleRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class RoleRepositoryTest {
    @Autowired
    private RoleRepository repo;
    @Test
    void testCreateFirstRole() {
        Role roleCustomer =  Role.builder().name("Customer").description("Customer").build();
        Role savedRole =  repo.save(roleCustomer);
        Assertions.assertThat(savedRole.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateRestRoles() {

        Role roleSalePerson =  Role.builder().name("Saleperson").description("Quản lý giá sản phẩm" +
                ", khách hàng, vận chuyển, đơn hàng và báo cáo doanh thu").build();



        Role roleShipper =  Role.builder().name("Shipper").description( "Xem sản phẩm, "
                + "Xem đơn hàng và cập nhật tình trạng đơn hàng.").build();

        Role roleAssisitant =  Role.builder().name("Assistant").description( "Quản lý các câu hỏi và đánh giá").build();
        Role roleCustomer = Role.builder().name("Customer").description("Khách han").build();
        repo.saveAll(java.util.List.of(roleSalePerson,roleShipper,roleAssisitant));
    }

}
