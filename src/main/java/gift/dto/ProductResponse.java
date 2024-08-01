package gift.dto;

import gift.model.Product;
import gift.model.ProductOption;

import java.util.List;
import java.util.stream.Collectors;

public class ProductResponse {
    private Long id;
    private String name;
    private Integer price;
    private String imageUrl;
    private Long categoryId;
    private List<ProductOptionResponse> options;

    public ProductResponse(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.imageUrl = product.getImageurl();
        this.categoryId = product.getCategory().getId();
        this.options = product.getOptions().stream().map(ProductOptionResponse::new).collect(Collectors.toList());
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public List<ProductOptionResponse> getOptions() {
        return options;
    }

    public void setOptions(List<ProductOptionResponse> options) {
        this.options = options;
    }
}
