package gift.controller;

import gift.dto.DomainResponse;
import gift.model.Category;
import gift.model.HttpResult;
import gift.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Category API", description = "APIs related to category operations")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Operation(summary = "카테고리 목록 조회", description = "모든 카테고리의 목록을 조회한다.")
    @GetMapping
    public ResponseEntity<DomainResponse> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        List<Map<String, Object>> categoryList = categories.stream()
                .map(category -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", category.getId());
                    map.put("name", category.getName());
                    map.put("color", category.getColor());
                    map.put("imageUrl", category.getImageUrl());
                    map.put("description", category.getDescription());
                    return map;
                })
                .collect(Collectors.toList());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Categories retrieved successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, categoryList, HttpStatus.OK));
    }

    @Operation(summary = "카테고리 생성", description = "새 카테고리를 등록한다.")
    @PostMapping
    public ResponseEntity<DomainResponse> createCategory(@RequestBody Category category) {
        Category createdCategory = categoryService.save(category);
        Map<String, Object> createdCategoryMap = new HashMap<>();
        createdCategoryMap.put("id", createdCategory.getId());
        createdCategoryMap.put("name", createdCategory.getName());
        createdCategoryMap.put("color", createdCategory.getColor());
        createdCategoryMap.put("imageUrl", createdCategory.getImageUrl());
        createdCategoryMap.put("description", createdCategory.getDescription());
        HttpResult httpResult = new HttpResult(HttpStatus.CREATED.value(), "Category created successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(new DomainResponse(httpResult, List.of(createdCategoryMap), HttpStatus.CREATED));
    }

    @Operation(summary = "카테고리 수정", description = "기존 카테고리를 수정한다.")
    @PutMapping("/{categoryId}")
    public ResponseEntity<DomainResponse> updateCategory(@PathVariable Long categoryId, @RequestBody Category category) {
        Category updatedCategory = categoryService.update(categoryId, category);
        Map<String, Object> updatedCategoryMap = new HashMap<>();
        updatedCategoryMap.put("id", updatedCategory.getId());
        updatedCategoryMap.put("name", updatedCategory.getName());
        updatedCategoryMap.put("color", updatedCategory.getColor());
        updatedCategoryMap.put("imageUrl", updatedCategory.getImageUrl());
        updatedCategoryMap.put("description", updatedCategory.getDescription());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Category updated successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, List.of(updatedCategoryMap), HttpStatus.OK));
    }

    @Operation(summary = "카테고리 조회", description = "카테고리 하나를 카테고리 아이디로 조회한다.")
    @GetMapping("/{categoryId}")
    public ResponseEntity<DomainResponse> getCategoryById(@PathVariable Long categoryId) {
        Category category = categoryService.findById(categoryId);
        if (category == null) {
            HttpResult httpResult = new HttpResult(HttpStatus.NOT_FOUND.value(), "Category not found");
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        Map<String, Object> categoryMap = new HashMap<>();
        categoryMap.put("id", category.getId());
        categoryMap.put("name", category.getName());
        categoryMap.put("color", category.getColor());
        categoryMap.put("imageUrl", category.getImageUrl());
        categoryMap.put("description", category.getDescription());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Category retrieved successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(categoryMap), HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "카테고리 삭제", description = "카테고리 아이디로 카테고리 하나를 삭제한다.")
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<DomainResponse> deleteCategory(@PathVariable Long categoryId) {
        categoryService.delete(categoryId);
        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Category deleted successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }
}