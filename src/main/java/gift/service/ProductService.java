package gift.service;

import gift.model.Category;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.CategoryRepository;
import gift.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductOptionService productOptionService;
    private final CategoryRepository categoryRepository;

    public ProductService(ProductRepository productRepository, ProductOptionService productOptionService, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productOptionService = productOptionService;
        this.categoryRepository = categoryRepository;
    }

    public void save(Product product) {
        validateProductOptions(product);

        // Ensure category exists
        Category category = categoryRepository.findById(product.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
        product.setCategory(category);

        // Save the product first
        Product savedProduct = productRepository.save(product);

        // Prepare and save product options
        List<ProductOption> options = product.getOptions().stream()
                .map(option -> {
                    ProductOption newOption = new ProductOption();
                    newOption.setName(option.getName());
                    newOption.setQuantity(option.getQuantity());
                    newOption.setProduct(savedProduct);
                    return newOption;
                })
                .collect(Collectors.toList());

        try {
            productOptionService.saveProductOptions(options);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    public Page<Product> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public void update(Product updatedProduct) {
        validateProductOptions(updatedProduct);

        // Ensure category exists
        Category category = categoryRepository.findById(updatedProduct.getCategory().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Category not found"));
        updatedProduct.setCategory(category);

        // Delete existing options and add new ones
        productOptionService.deleteProductOptionsByProductId(updatedProduct.getId());
        List<ProductOption> options = updatedProduct.getOptions().stream()
                .map(option -> {
                    ProductOption newOption = new ProductOption();
                    newOption.setName(option.getName());
                    newOption.setQuantity(option.getQuantity());
                    newOption.setProduct(updatedProduct);
                    return newOption;
                })
                .collect(Collectors.toList());

        try {
            productOptionService.saveProductOptions(options);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }

        productRepository.save(updatedProduct);
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found"));
    }

    public void delete(Long id) {
        productOptionService.deleteProductOptionsByProductId(id);
        productRepository.deleteById(id);
    }

    private void validateProductOptions(Product product) {
        List<ProductOption> options = product.getOptions();
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("At least one option is required.");
        }
    }
}
