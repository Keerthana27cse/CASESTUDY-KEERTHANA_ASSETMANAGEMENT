package com.example.assetmanagement.integrationtest;

import com.example.assetmanagement.dto.AssetCategoryDTO;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.repository.AssetCategoryRepo;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@Rollback
public class CategoryRestIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AssetCategoryRepo categoryRepo;

    @Autowired
    private ObjectMapper objectMapper;

    private AssetCategory testCategory;

    @BeforeEach
    public void setup() {
        categoryRepo.deleteAll();

        testCategory = new AssetCategory();
        testCategory.setCategoryName("Test Category");

        categoryRepo.save(testCategory);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenGetAllCategories_thenReturnList() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].categoryName").value(testCategory.getCategoryName()));
    }

    @Test
    @WithMockUser(roles = {"EMPLOYEE"})
    public void givenEmployee_whenGetAllCategories_thenReturnList() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].categoryName").value(testCategory.getCategoryName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenGetCategoryById_thenReturnCategory() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", testCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.categoryName").value(testCategory.getCategoryName()));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenGetCategoryByInvalidId_thenNotFound() throws Exception {
        mockMvc.perform(get("/api/categories/{id}", 9999L))
            .andExpect(status().isNotFound());
    }

    // Add category (admin only)
    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenAddCategory_thenCreated() throws Exception {
        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setCategoryName("New Category");

        mockMvc.perform(post("/api/categories/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isCreated())
            .andExpect(content().string("Category added successfully!"));

        List<AssetCategory> categories = categoryRepo.findAll();
        assertThat(categories).extracting("categoryName").contains("New Category");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenUpdateCategory_thenSuccess() throws Exception {
        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setCategoryName("Updated Category");

        mockMvc.perform(put("/api/categories/{id}", testCategory.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isOk())
            .andExpect(content().string("Category Updated Successfully"));

        AssetCategory updated = categoryRepo.findById(testCategory.getId()).orElse(null);
        assertThat(updated).isNotNull();
        assertThat(updated.getCategoryName()).isEqualTo("Updated Category");
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenUpdateCategoryInvalidId_thenNotFound() throws Exception {
        AssetCategoryDTO dto = new AssetCategoryDTO();
        dto.setCategoryName("Doesn't Matter");

        mockMvc.perform(put("/api/categories/{id}", 9999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Invalid Id"));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenDeleteCategory_thenSuccess() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", testCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().string("Category deleted successfully."));

        boolean exists = categoryRepo.existsById(testCategory.getId());
        assertThat(exists).isFalse();
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void givenAdmin_whenDeleteCategoryInvalidId_thenNotFound() throws Exception {
        mockMvc.perform(delete("/api/categories/{id}", 9999L))
            .andExpect(status().isNotFound())
            .andExpect(content().string("Invalid category ID."));
    }

    @Test
    public void whenNoAuth_thenUnauthorized() throws Exception {
        mockMvc.perform(get("/api/categories"))
            .andExpect(status().isUnauthorized());
    }
}
