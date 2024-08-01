package gift.dto;

public class WishResponse {
    private Long id;
    private Long productId;
    private int quantity;
    private Long optionId;

    public WishResponse() {
    }

    public WishResponse(Long id, Long productId, int quantity, Long optionId) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.optionId = optionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }
}
