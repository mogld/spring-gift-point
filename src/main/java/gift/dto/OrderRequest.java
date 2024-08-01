package gift.dto;

import java.time.LocalDateTime;

public class OrderRequest {
    private Long productId;
    private Long optionId;
    private int quantity;
    private LocalDateTime orderDateTime;
    private String message;
    private int pointsToUse;

    public OrderRequest() {}

    public OrderRequest(Long productId, Long optionId, int quantity, LocalDateTime orderDateTime, String message, int pointsToUse) {
        this.productId = productId;
        this.optionId = optionId;
        this.quantity = quantity;
        this.orderDateTime = orderDateTime;
        this.message = message;
        this.pointsToUse = pointsToUse;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPointsToUse() {
        return pointsToUse;
    }

    public void setPointsToUse(int pointsToUse) {
        this.pointsToUse = pointsToUse;
    }
}
