package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.OrderRequest;
import gift.dto.OrderResponse;
import gift.model.Member;
import gift.model.Order;
import gift.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/api/orders")
@Tag(name = "Order API", description = "APIs related to order operations")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Operation(summary = "주문하기", description = "새 주문을 생성한다.")
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest, @LoginMember Member member) {
        Order order = orderService.createOrder(orderRequest.getOptionId(), orderRequest.getQuantity(), orderRequest.getMessage(), member);
        OrderResponse response = new OrderResponse(order);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "주문 목록 조회(페이지네이션 적용)", description = "주문 목록을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<Page<OrderResponse>> getOrders(Pageable pageable) {
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(orders);
    }
}
