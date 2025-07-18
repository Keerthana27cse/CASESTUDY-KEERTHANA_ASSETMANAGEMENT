package com.example.assetmanagement.repotest;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.enums.AssetStatus;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.example.assetmanagement.repository.AssetRepo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class AssetRepoTest {

    @Autowired
    private AssetRepo assetRepo;

    @Autowired
    private AssetCategoryRepo assetCategoryRepo;

    private AssetCategory electronicsCategory;
    private Asset asset;

    @BeforeEach
    void setUp() {
        // Create and save category
        electronicsCategory = new AssetCategory();
        electronicsCategory.setCategoryName("Electronics");
        electronicsCategory = assetCategoryRepo.save(electronicsCategory);

        // Create and save asset
        asset = new Asset();
        asset.setAssetNo("AST001");
        asset.setAssetName("Laptop");
        asset.setAssetModel("HP EliteBook");
        asset.setDescription("14-inch business laptop");
        asset.setImageUrl("http://example.com/laptop.jpg");
        asset.setManufacturingDate(LocalDate.of(2023, 1, 1));
        asset.setExpiryDate(LocalDate.of(2026, 1, 1));
        asset.setAssetValue(55000.0);
        asset.setAssetStatus(AssetStatus.AVAILABLE);
        asset.setCategory(electronicsCategory);
        assetRepo.save(asset);
    }

    @Test
    void testFindByAssetNameContainingIgnoreCase() {
        List<Asset> result = assetRepo.findByAssetNameContainingIgnoreCase("laptop");
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindFirstByCategoryAndStatus() {
        Optional<Asset> result = assetRepo.findFirstByCategory_IdAndAssetStatus(
                electronicsCategory.getId(), AssetStatus.AVAILABLE
        );
        assertThat(result).isPresent();
        assertThat(result.get().getAssetName()).isEqualTo("Laptop");
    }

    @Test
    void testFindByCategoryId() {
        List<Asset> result = assetRepo.findByCategory_Id(electronicsCategory.getId());
        assertThat(result).hasSize(1);
    }

    @Test
    void testFindByCategoryIdAndAssetNameContainingIgnoreCase() {
        List<Asset> result = assetRepo.findByCategory_IdAndAssetNameContainingIgnoreCase(
                electronicsCategory.getId(), "lap"
        );
        assertThat(result).hasSize(1);
    }

    @Test
    void testExistsByAssetName() {
        boolean exists = assetRepo.existsByAssetName("Laptop");
        assertThat(exists).isTrue();
    }

    @Test
    void testFindByAssetNo() {
        Optional<Asset> result = assetRepo.findByAssetNo("AST001");
        assertThat(result).isPresent();
        assertThat(result.get().getAssetModel()).isEqualTo("HP EliteBook");
    }

    @Test
    void testFindByAssetName() {
        Asset result = assetRepo.findByAssetName("Laptop");
        assertThat(result).isNotNull();
        assertThat(result.getAssetNo()).isEqualTo("AST001");
    }
}
