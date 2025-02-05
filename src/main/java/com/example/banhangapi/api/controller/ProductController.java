package com.example.banhangapi.api.controller;

import com.example.banhangapi.api.entity.ProductDTO;
import com.example.banhangapi.api.entity.Product;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.banhangapi.api.service.ProductService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produce")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("create")
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> create(@Valid @RequestBody RequestCreateProduct requestCreateProduct) {
        return this.productService.save(requestCreateProduct);
    }

    @GetMapping("getAll")
    public ResponseEntity<Page<Product>> getAll(@RequestParam(required = false) int page, @RequestParam(required = false) int size) {
        return new ResponseEntity<>(this.productService.getAll(page, size), HttpStatus.OK);
    }

    @RequestMapping(value = "update", method = RequestMethod.PUT)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody RequestCreateProduct produce, @RequestParam Long id) {
        return this.productService.updateProduct(produce, id);
    }

    @RequestMapping(value = "delete", method = RequestMethod.DELETE)
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> delete(@RequestParam Long id) {
        return this.productService.deleteProduce(id);
    }

    @RequestMapping(value = "dataProduce", method = RequestMethod.GET)
    public ResponseEntity<ProductDTO> getDataProduce(@RequestParam Long id) {
        return new ResponseEntity<>(this.productService.getDataProduceById(id), HttpStatus.OK);
    }

    @RequestMapping(value = "get-list", method = RequestMethod.GET)
    public Object getList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        return  this.productService.getProducts(page, size);
    }

    @RequestMapping(value = "search", method = RequestMethod.GET)
    public List<Product> search(@RequestParam(required = false) String nameProduce, @RequestParam(required = false) Long minPrice, @RequestParam(required = false) Long maxPrice) {
        RequestSearchProduct requestSearchProduct = new RequestSearchProduct(
                nameProduce, minPrice, maxPrice
        );
        return this.productService.searchProduct(requestSearchProduct);
    }
}
