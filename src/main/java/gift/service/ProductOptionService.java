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
        Optional<ProductOption> productOption = productOptionRepository.findById(id);
        return productOption.orElse(null);
    }

    public void addProductOptions(Long productId, List<ProductOption> productOptions) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        for (ProductOption option : productOptions) {
            option.setProduct(product);
            validateProductOption(option);
            productOptionRepository.save(option);
        }
    }

    public void subtractProductOptionQuantity(Long optionId, int quantityToSubtract) {
        ProductOption option = findProductOptionById(optionId);
        if (option == null) {
            throw new IllegalArgumentException("Option not found");
        }
        option.subtractQuantity(quantityToSubtract);
        productOptionRepository.save(option);
    }


    public ProductOption updateProductOption(Long productId, Long optionId, String name, int quantity) {
        ProductOption existingOption = productOptionRepository.findById(optionId).orElseThrow(() -> new IllegalArgumentException("Option not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found"));
        existingOption.setName(name);
        existingOption.setQuantity(quantity);
        existingOption.setProduct(product);
        validateProductOption(existingOption);
        return productOptionRepository.save(existingOption);
    }

    public void deleteProductOption(Long productId, Long optionId) {
        productOptionRepository.deleteById(optionId);
    }

    private void validateProductOption(ProductOption option) {
        if (option.getName() == null || option.getName().isEmpty() || option.getName().length() > 50 ||
                !option.getName().matches("^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$")) {
            throw new IllegalArgumentException("옵션 이름은 공백을 포함하여 최대 50자까지 입력할 수 있으며, 특수 문자는 (),[],+,-,&,/,_만 가능합니다.");
        }
        if (option.getQuantity() < 1 || option.getQuantity() >= 100000000) {
            throw new IllegalArgumentException("옵션 수량은 최소 1개 이상 1억 개 미만이어야 합니다.");
        }
    }
}
