package com.example.banhangapi.api.service;

import com.example.banhangapi.api.dto.CategoryResponseDTO;
import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.request.CategoryRequest;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> createNewProduct(RequestCreateProduct requestCreateProduct);

    Page<ProductDTO> getListProduct(int page, int size, Long minPrice, Long maxPrice, Integer rating, String categoryId, String nameProduct, String sort);

    ResponseEntity<?> updateInfoProduct(String id, RequestCreateProduct product);

    ResponseEntity<?> deleteProduct(String id);

    ProductDTO getDataProduct(String id);

    String uploadImageForProduct(MultipartFile file, String id, boolean isDefault);

    Category createCategory(CategoryRequest categoryRequest);
    List<ProductDTO> getListProductByCategory(String categoryID);
    List<CategoryResponseDTO> getListCategory();
}
