package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.request.CategoryRequest;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.banhangapi.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/product")
@FieldDefaults(level = AccessLevel.PRIVATE)
@CrossOrigin(origins = "http://localhost:3000")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody RequestCreateProduct requestCreateProduct) {
        return this.productService.createNewProduct(requestCreateProduct);
    }


    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
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
    public Object getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return this.productService.getListProduct(page, size);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<ProductEntity> search(@RequestParam(required = false) String nameProduce, @RequestParam(required = false) Long minPrice, @RequestParam(required = false) Long maxPrice) {
        RequestSearchProduct requestSearchProduct = new RequestSearchProduct(
                nameProduce, minPrice, maxPrice
        );
        return this.productService.searchProduct(requestSearchProduct);
    }
    @PostMapping("upload-image")
    public ResponseEntity<?> uploadImage(@RequestPart MultipartFile file, @RequestParam String productId, @RequestPart() boolean isDefault) {
        return ResponseEntity.ok(productService.uploadImageForProduct(file, productId, isDefault));
    }

    @PostMapping("create-new-category")
    public ResponseEntity<Object> createCategory(CategoryRequest categoryRequest) {
        return ResponseEntity.ok(productService.createCategory(categoryRequest));
    }
    @GetMapping("get-list-product-by-category")
    public ResponseEntity<List<ProductDTO>> getListProductByCategory(@RequestParam String categoryId) {
        return ResponseEntity.ok(productService.getListProductByCategory(categoryId));
    }
    @GetMapping("get-list-category")
    public ResponseEntity<Object> getListCategory(){
        return ResponseEntity.ok(null);
    }
}
