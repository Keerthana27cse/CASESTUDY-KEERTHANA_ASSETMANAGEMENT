package com.example.assetmanagement.apitest;

import com.example.assetmanagement.dto.AssetDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.mapper.AssetMapper;
import com.example.assetmanagement.restcontroller.AssetRest;
import com.example.assetmanagement.service.AssetService;
import com.example.assetmanagement.service.CategoryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssetRestTest {

    @InjectMocks
    private AssetRest assetRest;

    @Mock
    private AssetService assetService;

    @Mock
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllAssets() {
        Asset asset = new Asset();
        asset.setId(1L);

        AssetDTO dto = new AssetDTO();
        dto.setId(1L);

        when(assetService.getAllAssets()).thenReturn(List.of(asset));

        try (MockedStatic<AssetMapper> mapperMock = mockStatic(AssetMapper.class)) {
            mapperMock.when(() -> AssetMapper.toDTO(asset)).thenReturn(dto);

            ResponseEntity<List<AssetDTO>> response = assetRest.getAllAssets();

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals(1L, response.getBody().get(0).getId());
        }
    }

    @Test
    void testGetAssetById() {
        Asset asset = new Asset();
        asset.setId(2L);

        AssetDTO dto = new AssetDTO();
        dto.setId(2L);

        when(assetService.getAssetById(2L)).thenReturn(asset);

        try (MockedStatic<AssetMapper> mapperMock = mockStatic(AssetMapper.class)) {
            mapperMock.when(() -> AssetMapper.toDTO(asset)).thenReturn(dto);

            ResponseEntity<AssetDTO> response = assetRest.getAssetById(2L);

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(2L, response.getBody().getId());
        }
    }

    @Test
    void testFilterAssets() {
        Asset asset = new Asset();
        asset.setId(3L);

        AssetDTO dto = new AssetDTO();
        dto.setId(3L);

        when(assetService.filterAssetByCategory(1L, "keyword")).thenReturn(List.of(asset));

        try (MockedStatic<AssetMapper> mapperMock = mockStatic(AssetMapper.class)) {
            mapperMock.when(() -> AssetMapper.toDTO(asset)).thenReturn(dto);

            ResponseEntity<List<AssetDTO>> response = assetRest.filterAssets(1L, "keyword");

            assertEquals(200, response.getStatusCodeValue());
            assertEquals(1, response.getBody().size());
            assertEquals(3L, response.getBody().get(0).getId());
        }
    }

    @Test
    void testSaveOrUpdateAsset_Success() throws IOException {
        AssetCategory category = new AssetCategory();
        category.setId(5L);

        AssetDTO dto = new AssetDTO();
        dto.setCategoryId(5L);

        Asset asset = new Asset();
        asset.setId(5L);

        MockMultipartFile imageFile = new MockMultipartFile("imageFile", new byte[0]);

        when(categoryService.getCategoryById(5L)).thenReturn(category);

        try (MockedStatic<AssetMapper> mapperMock = mockStatic(AssetMapper.class)) {
            mapperMock.when(() -> AssetMapper.toEntity(dto, category)).thenReturn(asset);

            when(assetService.saveOrUpdateAsset(asset, imageFile)).thenReturn(asset);

            mapperMock.when(() -> AssetMapper.toDTO(asset)).thenReturn(dto);

            ResponseEntity<?> response = assetRest.saveOrUpdateAsset(dto, imageFile);

            assertEquals(200, ((ResponseEntity<?>) response).getStatusCodeValue());
            assertEquals(dto, response.getBody());
        }
    }

    @Test
    void testSaveOrUpdateAsset_InvalidCategory() {
        AssetDTO dto = new AssetDTO();
        dto.setCategoryId(999L);

        when(categoryService.getCategoryById(999L)).thenReturn(null);

        ResponseEntity<?> response = assetRest.saveOrUpdateAsset(dto, null);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Invalid category ID", response.getBody());
    }

    @Test
    void testDeleteAsset_Success() {
        when(assetService.deleteAsset(10L)).thenReturn(true);

        ResponseEntity<String> response = assetRest.deleteAsset(10L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Asset deleted successfully.", response.getBody());
    }

    @Test
    void testDeleteAsset_NotFound() {
        when(assetService.deleteAsset(20L)).thenReturn(false);

        ResponseEntity<String> response = assetRest.deleteAsset(20L);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Asset not found.", response.getBody());
    }
}
