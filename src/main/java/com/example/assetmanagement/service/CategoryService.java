package com.example.assetmanagement.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.assetmanagement.entity.AssetCategory;
import com.example.assetmanagement.repository.AssetCategoryRepo;



@Service
public class CategoryService {
	
	@Autowired AssetCategoryRepo categoryRepo;
	
	
	
	
	public List<AssetCategory> getAllCategories()
	{
		return categoryRepo.findAll();
	}
	
	public AssetCategory getCategoryById(Long id)
	{
		return categoryRepo.findById(id).orElse(null);
	}
	
	public String addCategory(AssetCategory category) {
	    if (category.getCategoryName() == null || category.getCategoryName().trim().isEmpty()) {
	        throw new IllegalArgumentException("Category Name must not be Null or Empty");
	    }

	    boolean exists = categoryRepo.findByCategoryName(category.getCategoryName().trim())
	                                 .isPresent();
	    if (exists) {
	        throw new IllegalArgumentException("Category '" + category.getCategoryName() + "' already exists");
	    }

	    categoryRepo.save(category);
	    return "Category added successfully!";
	}

	
	public String updateCategory (Long id,AssetCategory category)
	{
		if(!categoryRepo.existsById(id))
		{
			return "Invalid Id";
		}
		category.setId(id);
		categoryRepo.save(category);
		return "Category Updated Successfully";
	}
	
	public  boolean deleteCategory(Long id)
	{
		if(categoryRepo.existsById(id))
		{
			categoryRepo.deleteById(id);
			return true;
		}
		return false;
	}
}
