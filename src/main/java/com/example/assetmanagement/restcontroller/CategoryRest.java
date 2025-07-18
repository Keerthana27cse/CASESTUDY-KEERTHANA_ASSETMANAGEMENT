package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.AssetCategoryDTO;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.mapper.AssetCategoryMapper;
import com.example.assetmanagement.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
public class CategoryRest {

    @Autowired
    private CategoryService categoryService;

    // ------------------- GET ALL ---------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<AssetCategoryDTO>> getAllCategories() {
        List<AssetCategoryDTO> dtos = categoryService.getAllCategories()
                .stream()
                .map(AssetCategoryMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ------------------- GET BY ID ---------------------
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<AssetCategoryDTO> getCategoryById(@PathVariable Long id) {
        AssetCategory category = categoryService.getCategoryById(id);
        if (category == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(AssetCategoryMapper.toDTO(category));
    }

    // ------------------- ADD CATEGORY (ADMIN) ---------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/save")
    public ResponseEntity<String> addCategory(@Valid @RequestBody AssetCategoryDTO dto) {
        String msg = categoryService.addCategory(AssetCategoryMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(msg);
    }

    // ------------------- UPDATE CATEGORY (ADMIN) ---------------------
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategory(@PathVariable Long id, @Valid @RequestBody AssetCategoryDTO dto) {
        String msg = categoryService.updateCategory(id, AssetCategoryMapper.toEntity(dto));
        if (msg.equals("Invalid Id")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    // ------------------- DELETE CATEGORY (ADMIN) ---------------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        boolean deleted = categoryService.deleteCategory(id);
        if (deleted) return ResponseEntity.ok("Category deleted successfully.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Invalid category ID.");
    }
}
