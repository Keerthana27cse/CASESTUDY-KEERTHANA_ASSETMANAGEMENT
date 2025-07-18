package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.AssetCategoryDTO;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.mapper.AssetCategoryMapper;
import com.example.assetmanagement.restcontroller.CategoryRest;
import com.example.assetmanagement.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryRestTest {

    @InjectMocks
    private CategoryRest categoryRest;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllCategories() {
        AssetCategory category = new AssetCategory();
        category.setId(1L);
        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setId(1L);

        when(categoryService.getAllCategories()).thenReturn(List.of(category));

        try (MockedStatic<AssetCategoryMapper> mocked = mockStatic(AssetCategoryMapper.class)) {
            mocked.when(() -> AssetCategoryMapper.toDTO(category)).thenReturn(dto);

            ResponseEntity<List<AssetCategoryDTO>> response = categoryRest.getAllCategories();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals(1L, response.getBody().get(0).getId());
        }
    }

}
