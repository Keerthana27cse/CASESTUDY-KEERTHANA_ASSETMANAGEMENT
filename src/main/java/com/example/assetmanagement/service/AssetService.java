package com.example.assetmanagement.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.repository.AssetRepo;

@Service
public class AssetService {

    @Autowired
    private AssetRepo assetRepo;

   
    @Value("${asset.upload.dir}")
    private String uploadDir;

    @Value("${asset.image.base-url}")
    private String baseUrl;

    public List<Asset> getAllAssets() {
        return assetRepo.findAll();
    }

    public Asset getAssetById(Long id) {
        return assetRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No Asset Found with ID " + id));
    }

    public List<Asset> filterAssetByCategory(Long categoryId, String keyword) {
        List<Asset> assets;

        if (categoryId == null || categoryId == 0) {
            assets = assetRepo.findAll();
        } else {
            assets = assetRepo.findByCategory_Id(categoryId);
        }

        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            assets = assets.stream()
                    .filter(asset -> asset.getAssetName().toLowerCase().contains(lowerKeyword))
                    .collect(Collectors.toList());
        }

        return assets;
    }

    public boolean deleteAsset(Long id) {
        if (assetRepo.existsById(id)) {
            Asset asset = assetRepo.findById(id).orElse(null);

            if (asset != null && asset.getImageUrl() != null) {
                Path uploadPath = Paths.get(uploadDir);
                String imageUrl = asset.getImageUrl();
                String fileName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
                Path filePath = uploadPath.resolve(fileName);

                try {
                    Files.deleteIfExists(filePath); 
                } catch (IOException e) {
                    System.err.println("Failed to delete image: " + e.getMessage());
                }
            }

            assetRepo.deleteById(id);
            return true;
        }

        return false;
    }

    public Asset saveOrUpdateAsset(Asset asset, MultipartFile imageFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        if (asset.getId() == null) {
            if (assetRepo.existsByAssetName(asset.getAssetName())) {
                throw new IllegalArgumentException("Asset with the same name already exists");
            }
        } else {
            Asset existingByName = assetRepo.findByAssetName(asset.getAssetName());
            if (existingByName != null && !existingByName.getId().equals(asset.getId())) {
                throw new IllegalArgumentException("Asset with the same name already exists");
            }

            Asset existing = assetRepo.findById(asset.getId()).orElse(null);
            if (existing != null && existing.getImageUrl() != null) {
                String imageUrl = existing.getImageUrl();
                String oldFilename = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);

                if (!oldFilename.isBlank()) {
                    Path oldFile = uploadPath.resolve(oldFilename);

                    if (Files.exists(oldFile) && Files.isRegularFile(oldFile)) {
                        Files.deleteIfExists(oldFile);
                    }
                }
            }

        }

        if (imageFile != null && !imageFile.isEmpty()) {
            String contentType = imageFile.getContentType();
            long size = imageFile.getSize();

            if (contentType == null || !contentType.startsWith("image/")) {
                throw new IllegalArgumentException("Only image files are allowed");
            }
            if (size > 2 * 1024 * 1024) {
                throw new IllegalArgumentException("Image size must be under 2MB");
            }

            if (asset.getId() != null) {
                Asset existing = assetRepo.findById(asset.getId()).orElse(null);
                if (existing != null && existing.getImageUrl() != null) {
                    String oldFilename = existing.getImageUrl().substring(existing.getImageUrl().lastIndexOf("/") + 1);
                    Path oldFile = uploadPath.resolve(oldFilename);
                    Files.deleteIfExists(oldFile);
                }
            }

            String originalFilename = Paths.get(imageFile.getOriginalFilename()).getFileName().toString();
            String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
            Path filePath = uploadPath.resolve(uniqueFilename);
            Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            asset.setImageUrl(baseUrl + uniqueFilename);  
        } else if (asset.getId() != null) {
            Asset existing = assetRepo.findById(asset.getId()).orElse(null);
            if (existing != null) {
                asset.setImageUrl(existing.getImageUrl());
            }
        }

        Asset saved = assetRepo.save(asset);
        return assetRepo.findById(saved.getId()).orElse(saved);
    }
}
