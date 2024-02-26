package com.myshop.fullstackdemo;

import com.myshop.fullstackdemo.model.Category;
import com.myshop.fullstackdemo.repository.CategoryRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Set;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repo;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category();
        category.setName("Tiểu thiết");
        Category savedCategory=repo.save(category);
        Assertions.assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testCreateSubCategory (){
        Category parent = new Category();
        parent.setId(1L);
        Category subCategory = new Category();
        subCategory.setName("Ngôn tình");
        subCategory.setParent(parent);
        Category savedSubCategory = repo.save(subCategory);
        Assertions.assertThat(savedSubCategory.getId()).isGreaterThan(0);

    }

    @Test
    public void testGetCategory(){
        Category category = repo.findById(3).get();
        System.out.println(category.getName());
        Set<Category> children = category.getChildren();
        for(Category subCategory : children){
            System.out.println(subCategory.getName());
        }
        Assertions.assertThat(children.size()).isGreaterThan(0);
    }

    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = repo.findAll();
        for(Category category : categories){
            if(category.getParent() ==  null){
                System.out.println(category.getName());
                Set<Category> children = category.getChildren();
                for (Category subCategory : children){
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }
    }
    private void printChildren(Category parent, int subLevel){
        int newSubLevel = subLevel +1;
        Set<Category> children = parent.getChildren();
        for(Category subCategory : children){
            for(int i=0; i < newSubLevel ;i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory, newSubLevel);
        }
    }

}
