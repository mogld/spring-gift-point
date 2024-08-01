package gift.controller;

import gift.dto.DomainResponse;
import gift.model.Product;
import gift.model.ProductOption;
import gift.model.HttpResult;
import gift.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "APIs related to product operations")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(summary = "상품 목록 조회 (페이지네이션 적용)", description = "모든 상품의 목록을 페이지 단위로 조회한다.")
    @GetMapping
    public ResponseEntity<DomainResponse> getAllProducts(@RequestParam(defaultValue = "0") int page,
                                                         @RequestParam(defaultValue = "10") int size,
                                                         @RequestParam(defaultValue = "id,asc") String sort,
                                                         @RequestParam(required = false) Long categoryId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = productService.getAllProducts(pageable);
        List<Map<String, Object>> productList = products.stream()
                .map(product -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", product.getId());
                    map.put("name", product.getName());
                    map.put("price", product.getPrice());
                    map.put("imageUrl", product.getImageUrl());
                    map.put("categoryId", product.getCategory().getId());
                    map.put("options", product.getOptions().stream().map(option -> {
                        Map<String, Object> optionMap = new HashMap<>();
                        optionMap.put("name", option.getName());
                        optionMap.put("quantity", option.getQuantity());
                        return optionMap;
                    }).collect(Collectors.toList()));
                    return map;
                })
                .collect(Collectors.toList());
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Products retrieved successfully");
        Map<String, Object> data = new HashMap<>();
        data.put("totalElements", products.getTotalElements());
        data.put("totalPages", products.getTotalPages());
        data.put("first", products.isFirst());
        data.put("last", products.isLast());
        data.put("size", products.getSize());
        data.put("content", productList);
        data.put("number", products.getNumber());
        data.put("sort", products.getSort());
        data.put("numberOfElements", products.getNumberOfElements());
        data.put("pageable", products.getPageable());
        data.put("empty", products.isEmpty());
        return ResponseEntity.ok(new DomainResponse(httpResult, List.of(data), HttpStatus.OK));
    }

    @Operation(summary = "상품 생성", description = "새 상품을 등록한다.")
    @PostMapping
    public ResponseEntity<DomainResponse> addProduct(@RequestBody Product product, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), "Validation errors");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(errors), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        productService.save(product);
        HttpResult httpResult = new HttpResult(HttpStatus.CREATED.value(), "Product created successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.CREATED), HttpStatus.CREATED);
    }

    @Operation(summary = "상품 수정", description = "기존 상품의 정보를 수정한다.")
    @PutMapping("/{id}")
    public ResponseEntity<DomainResponse> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, Object> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error ->
                    errors.put(error.getField(), error.getDefaultMessage())
            );
            HttpResult httpResult = new HttpResult(HttpStatus.BAD_REQUEST.value(), "Validation errors");
            return new ResponseEntity<>(new DomainResponse(httpResult, List.of(errors), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }

        Product existingProduct = productService.findById(id);
        if (existingProduct == null) {
            HttpResult httpResult = new HttpResult(HttpStatus.NOT_FOUND.value(), "Product not found");
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }

        existingProduct.setCategory(updatedProduct.getCategory());
        existingProduct.setName(updatedProduct.getName());
        existingProduct.setPrice(updatedProduct.getPrice());
        existingProduct.setImageUrl(updatedProduct.getImageUrl());
        existingProduct.setOptions(updatedProduct.getOptions());

        productService.update(existingProduct);
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Product updated successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "상품 조회", description = "상품 하나를 상품 아이디로 조회한다.")
    @GetMapping("/{productId}")
    public ResponseEntity<DomainResponse> getProductById(@PathVariable Long productId) {
        Product product = productService.findById(productId);
        if (product == null) {
            HttpResult httpResult = new HttpResult(HttpStatus.NOT_FOUND.value(), "Product not found");
            return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
        }
        Map<String, Object> productMap = new HashMap<>();
        productMap.put("id", product.getId());
        productMap.put("name", product.getName());
        productMap.put("price", product.getPrice());
        productMap.put("imageUrl", product.getImageUrl());
        productMap.put("categoryId", product.getCategory().getId());
        productMap.put("options", product.getOptions().stream().map(option -> {
            Map<String, Object> optionMap = new HashMap<>();
            optionMap.put("name", option.getName());
            optionMap.put("quantity", option.getQuantity());
            return optionMap;
        }).collect(Collectors.toList()));
        HttpResult httpResult = new HttpResult(HttpStatus.OK.value(), "Product retrieved successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, List.of(productMap), HttpStatus.OK), HttpStatus.OK);
    }

    @Operation(summary = "상품 삭제", description = "상품 아이디로 하나의 상품을 삭제한다.")
    @DeleteMapping("/{productId}")
    public ResponseEntity<DomainResponse> deleteProduct(@PathVariable Long productId) {
        productService.delete(productId);
        HttpResult httpResult = new HttpResult(HttpStatus.NO_CONTENT.value(), "Product deleted successfully");
        return new ResponseEntity<>(new DomainResponse(httpResult, null, HttpStatus.NO_CONTENT), HttpStatus.NO_CONTENT);
    }
}
