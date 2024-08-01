package gift.service;

import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductOption> getOptionsByProductId(Long productId) {
        return productOptionRepository.findByProductId(productId);
    }

    public ProductOption findProductOptionById(Long id) {
        return productOptionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("ProductOption not found"));
    }

    public void saveProductOptions(List<ProductOption> productOptions) {
        for (ProductOption option : productOptions) {
            validateProductOption(option);
            productOptionRepository.save(option);
        }
    }

    public void subtractProductOptionQuantity(Long optionId, int quantityToSubtract) {
        ProductOption option = findProductOptionById(optionId);
        option.subtractQuantity(quantityToSubtract);
        productOptionRepository.save(option);
    }

    public ProductOption updateProductOption(Long productId, Long optionId, String name, int quantity) {
        ProductOption existingOption = findProductOptionById(optionId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingOption.setName(name);
        existingOption.setQuantity(quantity);
        existingOption.setProduct(product);
        validateProductOption(existingOption);
        return productOptionRepository.save(existingOption);
    }

    public void deleteProductOption(Long optionId) {
        if (!productOptionRepository.existsById(optionId)) {
            throw new IllegalArgumentException("ProductOption not found");
        }
        productOptionRepository.deleteById(optionId);
    }

    private void validateProductOption(ProductOption option) {
        if (option.getName() == null || option.getName().isEmpty() || option.getName().length() > 50 ||
                !option.getName().matches("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$")) {
            throw new IllegalArgumentException("Option name must be between 1 and 50 characters and can include only specific special characters.");
        }
        if (option.getQuantity() < 1 || option.getQuantity() >= 100000000) {
            throw new IllegalArgumentException("Option quantity must be between 1 and 100,000,000.");
        }
    }
    public void deleteProductOptionsByProductId(Long productId) {
        List<ProductOption> options = productOptionRepository.findByProductId(productId);
        productOptionRepository.deleteAll(options);
    }

}
