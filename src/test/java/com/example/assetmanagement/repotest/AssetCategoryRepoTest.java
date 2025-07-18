package com.example.assetmanagement.repotest;



import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.repository.AssetCategoryRepo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssetCategoryRepoTest {

    @Autowired
    private AssetCategoryRepo categoryRepo;

    @Test
    public void testSaveAndFindByCategoryName() {
        AssetCategory category = new AssetCategory();
        category.setCategoryName("Furniture");
        
        AssetCategory saved = categoryRepo.save(category);
        assertThat(saved.getId()).isNotNull();

        Optional<AssetCategory> found = categoryRepo.findByCategoryName("Furniture");
        assertThat(found).isPresent();
        assertThat(found.get().getCategoryName()).isEqualTo("Furniture");
    }
}
