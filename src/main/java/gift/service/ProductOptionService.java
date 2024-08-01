package gift.service;

import gift.dto.ProductOptionRequest;
import gift.dto.ProductOptionResponse;
import gift.model.Product;
import gift.model.ProductOption;
import gift.repository.ProductOptionRepository;
import gift.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductOptionService {

    @Autowired
    private ProductOptionRepository productOptionRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<ProductOptionResponse> getOptionsByProductId(Long productId) {
        return productOptionRepository.findByProductId(productId).stream()
                .map(ProductOptionResponse::new)
                .collect(Collectors.toList());
    }

    public ProductOptionResponse findById(Long id) {
        ProductOption productOption = productOptionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        return new ProductOptionResponse(productOption);
    }

    public void saveProductOptions(List<ProductOption> options) {
        productOptionRepository.saveAll(options);
    }

    public void deleteProductOptionsByProductId(Long productId) {
        List<ProductOption> options = productOptionRepository.findByProductId(productId);
        productOptionRepository.deleteAll(options);
    }

    public void subtractProductOptionQuantity(Long optionId, int quantityToSubtract) {
        ProductOption option = productOptionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        option.subtractQuantity(quantityToSubtract);
        productOptionRepository.save(option);
    }

    public ProductOptionResponse addProductOption(Long productId, ProductOptionRequest productOptionRequest) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        ProductOption productOption = new ProductOption();
        productOption.setProduct(product);
        productOption.setName(productOptionRequest.getName());
        productOption.setQuantity(productOptionRequest.getQuantity());
        ProductOption savedOption = productOptionRepository.save(productOption);
        return new ProductOptionResponse(savedOption);
    }

    public ProductOptionResponse updateProductOption(Long productId, Long optionId, ProductOptionRequest productOptionRequest) {
        ProductOption existingOption = productOptionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingOption.setName(productOptionRequest.getName());
        existingOption.setQuantity(productOptionRequest.getQuantity());
        existingOption.setProduct(product);
        ProductOption updatedOption = productOptionRepository.save(existingOption);
        return new ProductOptionResponse(updatedOption);
    }

    public void deleteProductOption(Long productId, Long optionId) {
        productOptionRepository.deleteById(optionId);
    }
}
