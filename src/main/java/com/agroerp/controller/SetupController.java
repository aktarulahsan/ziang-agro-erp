package com.agroerp.controller;

import com.agroerp.dto.SetupDtos.SubCategoryRequest;
import com.agroerp.dto.SetupDtos.SubCategoryResponse;
import com.agroerp.entity.*;
import com.agroerp.exception.ResourceNotFoundException;
import com.agroerp.repository.*;
import com.agroerp.response.ApiResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/setup")
public class SetupController {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final UnitRepository unitRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final WarehouseRepository warehouseRepository;
    private final TerritoryRepository territoryRepository;
    private final CompanyRepository companyRepository;

    public SetupController(CategoryRepository categoryRepository, BrandRepository brandRepository,
                           UnitRepository unitRepository, WarehouseRepository warehouseRepository,
                           SubCategoryRepository subCategoryRepository, TerritoryRepository territoryRepository,
                           CompanyRepository companyRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.unitRepository = unitRepository;
        this.subCategoryRepository = subCategoryRepository;
        this.warehouseRepository = warehouseRepository;
        this.territoryRepository = territoryRepository;
        this.companyRepository = companyRepository;
    }

    @GetMapping("/companies") public ApiResponse<List<Company>> companies() { return ApiResponse.ok("Companies loaded", companyRepository.findAll()); }
    @PostMapping("/companies") public ApiResponse<Company> company(@RequestBody Company item) { return ApiResponse.ok("Company saved", companyRepository.save(item)); }
    @GetMapping("/categories") public ApiResponse<List<Category>> categories() { return ApiResponse.ok("Categories loaded", categoryRepository.findAll()); }
    @PostMapping("/categories") public ApiResponse<Category> category(@RequestBody Category item) { return ApiResponse.ok("Category saved", categoryRepository.save(item)); }
    @GetMapping("/sub-categories")
    @Transactional(readOnly = true)
    public ApiResponse<List<SubCategoryResponse>> subCategories(@RequestParam(required = false) Long categoryId) {
        List<SubCategory> rows = categoryId == null ? subCategoryRepository.findAll() : subCategoryRepository.findByCategoryIdAndDeletedFalse(categoryId);
        return ApiResponse.ok("Sub categories loaded", rows.stream().map(this::toSubCategoryResponse).toList());
    }
    @PostMapping("/sub-categories")
    public ApiResponse<SubCategoryResponse> subCategory(@RequestBody SubCategoryRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        SubCategory item = new SubCategory();
        item.setName(request.name());
        item.setCategory(category);
        item.setActive(request.active());
        return ApiResponse.ok("Sub category saved", toSubCategoryResponse(subCategoryRepository.save(item)));
    }
    @GetMapping("/brands") public ApiResponse<List<Brand>> brands() { return ApiResponse.ok("Brands loaded", brandRepository.findAll()); }
    @PostMapping("/brands") public ApiResponse<Brand> brand(@RequestBody Brand item) { return ApiResponse.ok("Brand saved", brandRepository.save(item)); }
    @GetMapping("/units") public ApiResponse<List<Unit>> units() { return ApiResponse.ok("Units loaded", unitRepository.findAll()); }
    @PostMapping("/units") public ApiResponse<Unit> unit(@RequestBody Unit item) { return ApiResponse.ok("Unit saved", unitRepository.save(item)); }
    @GetMapping("/warehouses") public ApiResponse<List<Warehouse>> warehouses() { return ApiResponse.ok("Warehouses loaded", warehouseRepository.findAll()); }
    @PostMapping("/warehouses") public ApiResponse<Warehouse> warehouse(@RequestBody Warehouse item) { return ApiResponse.ok("Warehouse saved", warehouseRepository.save(item)); }
    @GetMapping("/territories") public ApiResponse<List<Territory>> territories() { return ApiResponse.ok("Territories loaded", territoryRepository.findAll()); }
    @PostMapping("/territories") public ApiResponse<Territory> territory(@RequestBody Territory item) { return ApiResponse.ok("Territory saved", territoryRepository.save(item)); }

    private SubCategoryResponse toSubCategoryResponse(SubCategory item) {
        return new SubCategoryResponse(item.getId(), item.getName(), item.getCategory().getId(),
                item.getCategory().getName(), item.isActive());
    }
}
