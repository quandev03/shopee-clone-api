package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.request.CategoryRequest;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.banhangapi.api.service.CartService;
import com.example.banhangapi.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CartService cartService;

    @PostMapping("create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ProductDTO> create(@RequestBody RequestCreateProduct requestCreateProduct) {
        return ResponseEntity.ok(this.productService.createNewProduct(requestCreateProduct));
    }


    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody RequestCreateProduct product, @RequestParam String id) {
        return new ResponseEntity<>(this.productService.updateInfoProduct(id, product), HttpStatus.OK);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam String id) {
        return this.productService.deleteProduct(id);
    }

    @RequestMapping(value = "dataProduct", method = RequestMethod.GET)
    public ResponseEntity<ProductDTO> getDataProduce(@RequestParam String id) {
        return new ResponseEntity<>(this.productService.getDataProduct(id), HttpStatus.OK);
    }

    @RequestMapping(value = "get-list", method = RequestMethod.GET)
    public ResponseEntity<Page<ProductDTO>> getList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10", name = "limit") int size,
            @RequestParam(required = false, name = "priceMin") Double minPrice,
            @RequestParam(required = false, name = "priceMax") Double maxPrice,
            @RequestParam(required = false, name = "category") String categoryId,
            @RequestParam(required = false, name = "rating") Integer rating,
            @RequestParam(required = false, name = "name") String nameProduct,
            @RequestParam(required = false, name = "sort", defaultValue = "name-product") String sort

    ){
        return ResponseEntity.ok(this.productService.getListProduct(page, size, minPrice, maxPrice, rating, categoryId,nameProduct, sort));
    }

    @PostMapping("upload-image")
    public ResponseEntity<?> uploadImage(@RequestPart("file") MultipartFile file,
                                         @RequestParam("productId") String productId,
                                         @RequestParam("isDefault") Boolean isDefault) {
        return ResponseEntity.ok(productService.uploadImageForProduct(file, productId, isDefault));
    }

    @PostMapping("create-new-category")
    public ResponseEntity<Object> createCategory(@RequestBody CategoryRequest categoryRequest) {
        return ResponseEntity.ok(productService.createCategory(categoryRequest));
    }
    @GetMapping("get-list-product-by-category")
    public ResponseEntity<List<ProductDTO>> getListProductByCategory(@RequestParam String categoryId) {
        return ResponseEntity.ok(productService.getListProductByCategory(categoryId));
    }
    @GetMapping("get-list-category")
    public ResponseEntity<Object> getListCategory(){
        return ResponseEntity.ok(productService.getListCategory());
    }

    @GetMapping("get-data-cart")
    public ResponseEntity<Object> getCart(){
        return ResponseEntity.ok(cartService.getAllCartOfUser());
    }

}
