package com.example.assetmanagement.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.assetmanagement.entity.Asset;
import com.example.assetmanagement.enums.AssetStatus;

public interface AssetRepo extends JpaRepository<Asset, Long> {


    // Find all assets belonging to a given category id

    // For search, you might add (example by assetName containing keyword)
    List<Asset> findByAssetNameContainingIgnoreCase(String keyword);

    Optional<Asset> findFirstByCategory_IdAndAssetStatus(Long categoryId, AssetStatus assetStatus);

    List<Asset> findByCategory_Id(Long categoryId);

	List<Asset> findByCategory_IdAndAssetNameContainingIgnoreCase(Long categoryId, String search);

	boolean existsByAssetName(String assetName);

	Optional<Asset> findByAssetNo(String string);

	Asset findByAssetName(String assetName);




}
