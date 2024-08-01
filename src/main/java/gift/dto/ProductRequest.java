package gift.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class ProductRequest {
    @NotBlank(message = "상품을 입력해주세요")
    @Size(max = 15, message = "상품 이름은 최대 15자까지 입력할 수 있습니다")
    @Pattern(regexp = "^[가-힣ㄱ-ㅎㅏ-ㅣa-zA-Z0-9\\s\\(\\)\\[\\]\\+\\-\\&\\/\\_]+$", message = "특수문자는 (),[],+,-,&,/,_만 가능합니다")
    @Pattern(regexp = "^(?!.*카카오).*$", message = "상품 이름에 '카카오'를 포함할 수 없습니다. 관리자와의 협의가 필요합니다.")
    private String name;

    @NotNull(message = "상품 가격을 입력해주세요")
    private Integer price;

    @NotBlank(message = "상품 이미지를 입력해주세요")
    private String imageUrl;

    @NotNull(message = "카테고리를 입력해주세요")
    private Long categoryId;

    @NotNull(message = "옵션을 입력해주세요")
    private List<ProductOptionRequest> options;

    // Getters and Setters
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

    public List<ProductOptionRequest> getOptions() {
        return options;
    }

    public void setOptions(List<ProductOptionRequest> options) {
        this.options = options;
    }
}
