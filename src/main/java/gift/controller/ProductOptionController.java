package gift.controller;

import gift.model.ProductOption;
import gift.service.ProductOptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products/{productId}/options")
@Tag(name = "Product Option API", description = "APIs related to product option operations")
public class ProductOptionController {

    @Autowired
    private ProductOptionService productOptionService;

    @Operation(summary = "상품 옵션 추가", description = "상품에 옵션을 추가한다.")
    @PostMapping
    public ResponseEntity<String> addProductOption(@PathVariable Long productId, @RequestBody ProductOption productOption) {
        productOptionService.addProductOption(productId, productOption);
        return ResponseEntity.ok("Option added successfully");
    }

    @Operation(summary = "상품 옵션 수정", description = "기존 상품 옵션의 정보를 수정한다.")
    @PutMapping("/{optionId}")
    public ResponseEntity<String> updateProductOption(@PathVariable Long productId, @PathVariable Long optionId, @RequestBody ProductOption productOption) {
        productOptionService.updateProductOption(productId, optionId, productOption);
        return ResponseEntity.ok("Option updated successfully");
    }

    @Operation(summary = "상품 옵션 삭제", description = "기존 상품 옵션을 삭제한다.")
    @DeleteMapping("/{optionId}")
    public ResponseEntity<Void> deleteProductOption(@PathVariable Long productId, @PathVariable Long optionId) {
        productOptionService.deleteProductOption(productId, optionId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "상품 옵션 목록 조회", description = "특정 상품에 대한 모든 옵션을 조회한다.")
    @GetMapping
    public ResponseEntity<List<ProductOption>> getProductOptions(@PathVariable Long productId) {
        List<ProductOption> options = productOptionService.getOptionsByProductId(productId);
        return ResponseEntity.ok(options);
    }
}
