package gift.model;

import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private int quantity;
    @Lob
    private String message;
    private LocalDateTime orderDateTime;


    public Order() {
    }

    public Order(ProductOption productOption, Member member, int quantity, String message, LocalDateTime orderDateTime) {
        this.productOption = productOption;
        this.member = member;
        this.quantity = quantity;
        this.setMessage(message);
        this.orderDateTime = orderDateTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProductOption getProductOption() {
        return productOption;
    }

    public void setProductOption(ProductOption productOption) {
        this.productOption = productOption;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getMessage() {

        return new String(Base64.getUrlDecoder().decode(this.message), StandardCharsets.UTF_8);
    }

    public void setMessage(String message) {

        this.message = Base64.getUrlEncoder().encodeToString(message.getBytes(StandardCharsets.UTF_8));
    }

    public LocalDateTime getOrderDateTime() {
        return orderDateTime;
    }

    public void setOrderDateTime(LocalDateTime orderDateTime) {
        this.orderDateTime = orderDateTime;
    }
}