package com.example.assetmanagement.restcontroller;

import com.example.assetmanagement.dto.AssetDTO;
import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.mapper.AssetMapper;
import com.example.assetmanagement.service.AssetService;
import com.example.assetmanagement.service.CategoryService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/assets")
public class AssetRest {

    @Autowired
    private AssetService assetService;
    
    @Autowired
    private CategoryService categoryService;

    // ---------------- GET ALL ASSETS ----------------
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAllAssets() {
        List<AssetDTO> dtos = assetService.getAllAssets()
                .stream()
                .map(AssetMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ---------------- GET ASSET BY ID ----------------
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(@PathVariable Long id) {
        Asset asset = assetService.getAssetById(id);
        return ResponseEntity.ok(AssetMapper.toDTO(asset));
    }

    // ---------------- FILTER ASSETS ----------------
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    @GetMapping("/filter")
    public ResponseEntity<List<AssetDTO>> filterAssets(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String keyword) {

        List<AssetDTO> dtos = assetService.filterAssetByCategory(categoryId, keyword)
                .stream()
                .map(AssetMapper::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // ---------------- ADD OR UPDATE ASSET ----------------
    @PostMapping("/save")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> saveOrUpdateAsset(
            @RequestPart("asset") @Valid AssetDTO assetDTO,
            @RequestPart(name = "imageFile", required = false) MultipartFile imageFile) {
        try {
            // Fetch category entity by ID, validate existence
            AssetCategory category = categoryService.getCategoryById(assetDTO.getCategoryId());
            if (category == null) {
                return ResponseEntity.badRequest().body("Invalid category ID");
            }

            // Map DTO to entity
            Asset asset = AssetMapper.toEntity(assetDTO, category);

            // Save asset and image
            Asset savedAsset = assetService.saveOrUpdateAsset(asset, imageFile);

            // Return saved asset DTO
            return ResponseEntity.ok(AssetMapper.toDTO(savedAsset));

        } catch (IOException | IllegalArgumentException e) {
            // Log and return error
            e.printStackTrace(); // Or use a logger
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to save asset: " + e.getMessage());
        }
    }

    // ---------------- DELETE ASSET ----------------
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(@PathVariable Long id) {
        boolean deleted = assetService.deleteAsset(id);
        if (deleted) {
            return ResponseEntity.ok("Asset deleted successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Asset not found.");
        }
    }
}
