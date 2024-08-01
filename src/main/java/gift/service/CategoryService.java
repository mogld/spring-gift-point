package gift.service;

import gift.model.Category;
import gift.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(Long categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        existingCategory.setName(category.getName());
        existingCategory.setColor(category.getColor());
        existingCategory.setImageUrl(category.getImageUrl());
        existingCategory.setDescription(category.getDescription());
        return existingCategory;
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
