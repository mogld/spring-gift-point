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
        Category category = categoryRepository.findById(product.getCategory().getId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        product.setCategory(category);
        Product savedProduct = productRepository.save(product);  // 상품을 먼저 저장
        List<ProductOption> options = product.getOptions().stream()
                .map(option -> {
                    ProductOption newOption = new ProductOption();
                    newOption.setName(option.getName());
                    newOption.setQuantity(option.getQuantity());
                    newOption.setProduct(savedProduct);  // 연관된 상품 설정
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
        Category category = categoryRepository.findById(updatedProduct.getCategory().getId()).orElseThrow(() -> new IllegalArgumentException("Category not found"));
        updatedProduct.setCategory(category);
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
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    public void delete(Long id) {
        productOptionService.deleteProductOptionsByProductId(id);
        productRepository.deleteById(id);
    }

    private void validateProductOptions(Product product) {
        List<ProductOption> options = product.getOptions();
        if (options == null || options.isEmpty()) {
            throw new IllegalArgumentException("1개 이상의 옵션이 필요합니다.");
        }
    }
}
