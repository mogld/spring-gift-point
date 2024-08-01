package gift.controller;

import gift.annotation.LoginMember;
import gift.dto.DomainResponse;
import gift.dto.WishRequest;
import gift.dto.WishResponse;
import gift.model.HttpResult;
import gift.model.Member;
import gift.model.Wish;
import gift.service.WishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/wishes")
@Tag(name = "Wish API", description = "APIs related to wish operations")
public class WishController {

    @Autowired
    private WishService wishService;

    @Operation(summary = "위시 리스트 상품 조회(페이지네이션 적용)", description = "회원의 위시 리스트에 있는 상품을 페이지 단위로 조회한다.")
    @GetMapping
    public DomainResponse getWishes(@LoginMember Member member, Pageable pageable) {
        Page<WishResponse> wishes = wishService.getWishesByMemberId(member.getId(), pageable);
        List<Map<String, Object>> wishList = wishes.stream()
                .map(wish -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", wish.getId());
                    map.put("productId", wish.getProductId());
                    map.put("quantity", wish.getQuantity());
                    map.put("optionId", wish.getOptionId());
                    return map;
                })
                .collect(Collectors.toList());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Wishes retrieved successfully");
        Map<String, Object> data = new HashMap<>();
        data.put("totalElements", wishes.getTotalElements());
        data.put("totalPages", wishes.getTotalPages());
        data.put("first", wishes.isFirst());
        data.put("last", wishes.isLast());
        data.put("size", wishes.getSize());
        data.put("content", wishList);
        data.put("number", wishes.getNumber());
        data.put("sort", wishes.getSort());
        data.put("numberOfElements", wishes.getNumberOfElements());
        data.put("pageable", wishes.getPageable());
        data.put("empty", wishes.isEmpty());
        return new DomainResponse(httpResult, List.of(data), HttpStatus.OK);
    }

    @Operation(summary = "위시 리스트 상품 추가", description = "회원의 위시 리스트에 상품을 추가한다.")
    @PostMapping
    public DomainResponse addWish(@RequestBody WishRequest wishRequest, @LoginMember Member member) {
        Wish wish = wishService.addWish(member.getId(), wishRequest.getProductId(), wishRequest.getOptionId(), wishRequest.getQuantity());
        Map<String, Object> wishMap = new HashMap<>();
        wishMap.put("id", wish.getId());
        wishMap.put("productId", wish.getProduct().getId());
        wishMap.put("quantity", wish.getQuantity());
        wishMap.put("optionId", wish.getProductOption().getId());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Wish added successfully");
        return new DomainResponse(httpResult, List.of(wishMap), HttpStatus.OK);
    }

    @Operation(summary = "위시 리스트 상품 수정", description = "위시리스트 아이디로 회원의 위시리스트를 수정합니다.")
    @PutMapping("/{wishId}")
    public DomainResponse updateWish(@PathVariable Long wishId, @RequestBody WishRequest wishRequest) {
        Wish wish = wishService.updateWish(wishId, wishRequest.getProductId(), wishRequest.getOptionId(), wishRequest.getQuantity());
        Map<String, Object> wishMap = new HashMap<>();
        wishMap.put("id", wish.getId());
        wishMap.put("productId", wish.getProduct().getId());
        wishMap.put("quantity", wish.getQuantity());
        wishMap.put("optionId", wish.getProductOption().getId());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Wish updated successfully");
        return new DomainResponse(httpResult, List.of(wishMap), HttpStatus.OK);
    }

    @Operation(summary = "위시 리스트 상품 삭제", description = "위시리스트 아이디로 하나의 위시리스트를 삭제합니다.")
    @DeleteMapping("/{wishId}")
    public DomainResponse deleteWish(@PathVariable Long wishId) {
        wishService.deleteWish(wishId);
        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Wish deleted successfully");
        return new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT);
    }
}
