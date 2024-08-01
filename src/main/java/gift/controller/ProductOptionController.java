package gift.controller;

import gift.dto.DomainResponse;
import gift.dto.ProductOptionRequest;
import gift.dto.ProductOptionResponse;
import gift.model.HttpResult;
import gift.model.ProductOption;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Product Option API", description = "APIs related to product option operations")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;

    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    @PostMapping
    public ResponseEntity<DomainResponse> addProductOption(@PathVariable Long productId, @RequestBody List<ProductOptionRequest> productOptionRequests) {
        List<ProductOption> productOptions = productOptionRequests.stream()
                .map(request -> {
                    ProductOption option = new ProductOption();
                    option.setName(request.getName());
                    option.setQuantity(request.getQuantity());
                    return option;
                })
                .collect(Collectors.toList());

        productOptionService.addProductOptions(productId, productOptions);

        List<Map<String, Object>> responseList = productOptions.stream()
                .map(option -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", option.getName());
                    map.put("quantity", option.getQuantity());
                    return map;
                })
                .collect(Collectors.toList());

        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Option added successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, responseList, HttpStatus.OK));
    }

    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션의 정보를 수정한다.")
    @PutMapping("/{optionId}")
    public ResponseEntity<DomainResponse> updateProductOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody ProductOptionRequest productOptionRequest) {
        ProductOption updatedOption = productOptionService.updateProductOption(productId, optionId, productOptionRequest.getName(), productOptionRequest.getQuantity());

        Map<String, Object> response = new HashMap<>();
        response.put("name", updatedOption.getName());
        response.put("quantity", updatedOption.getQuantity());

        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Option updated successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, List.of(response), HttpStatus.OK));
    }

    @Operation(summary = "상품 옵션 삭제", description = "기존 상품 옵션을 삭제한다.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<DomainResponse> deleteProductOption(@PathVariable Long productId, @PathVariable Long optionId) {
        productOptionService.deleteProductOption(productId, optionId);

        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Option deleted successfully");
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT));
    }

    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    @GetMapping
    public ResponseEntity<DomainResponse> getProductOptions(@PathVariable Long productId) {
        List<ProductOption> options = productOptionService.getOptionsByProductId(productId);

        List<Map<String, Object>> responseList = options.stream()
                .map(option -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", option.getName());
                    map.put("quantity", option.getQuantity());
                    return map;
                })
                .collect(Collectors.toList());

        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Options retrieved successfully");
        return ResponseEntity.ok(new DomainResponse(httpResult, responseList, HttpStatus.OK));
    }
}
