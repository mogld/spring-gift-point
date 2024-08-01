package gift.service;

import gift.dto.OrderResponse;
import gift.model.Member;
import gift.model.Order;
import gift.model.ProductOption;
import gift.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductOptionService productOptionService;

    @Autowired
    private WishService wishService;

    @Autowired
    private PointService pointService;

    @Transactional
    public Order createOrder(Long optionId, int quantity, String message, Member member, int pointsToUse) {
        ProductOption productOption = productOptionService.findProductOptionById(optionId);
        if (productOption == null) {
            throw new IllegalArgumentException("Invalid product option");
        }

        productOptionService.subtractProductOptionQuantity(optionId, quantity);

        // Use points
        if (pointsToUse > 0) {
            pointService.usePoints(member.getId(), pointsToUse);
        }

        Order order = new Order(productOption, member, quantity, message, LocalDateTime.now());

        wishService.deleteWishByProductOptionIdAndMemberId(optionId, member.getId());

        return orderRepository.save(order);
    }

    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderResponse::new);
    }
}
