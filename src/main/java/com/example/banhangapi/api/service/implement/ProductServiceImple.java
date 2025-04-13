package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.CategoryResponseDTO;
import com.example.banhangapi.api.dto.ImageResponseDTO;
import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.Category;
import com.example.banhangapi.api.entity.Image;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.mapper.CategoryMapper;
import com.example.banhangapi.api.mapper.ImageMapper;
import com.example.banhangapi.api.mapper.ProductMapper;
import com.example.banhangapi.api.repository.CategoryRepository;
import com.example.banhangapi.api.repository.ImageRepository;
import com.example.banhangapi.api.repository.ProductRepository;
import com.example.banhangapi.api.repository.ProductSpecification;
import com.example.banhangapi.api.request.CategoryRequest;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.service.ImageService;
import com.example.banhangapi.api.service.ProductService;
import com.example.banhangapi.helper.exception.ChangeSubscriberInformationErrorKey;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.redis.RedisServiceImpl;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class ProductServiceImple implements ProductService {
    final ProductRepository productRepository;

    final RedisServiceImpl redisService;

    final ProductMapper productMapper;

    final ImageService imageService;
    final ImageMapper imageMapper;
    private final ImageRepository imageRepository;
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    @Transactional
    public ProductDTO createNewProduct(RequestCreateProduct dataCreateNewProduct) {
        if (productRepository.existsByNameProduct(dataCreateNewProduct.getNameProduct())) {
            log.error("Sản phẩm đã tồn tại: {}", dataCreateNewProduct.getNameProduct());
            throw new IllegalArgumentException("Tên sản phẩm đã tồn tại");
        }

        try {
            ProductEntity dataNewProduct = productMapper.ReqToProduct(dataCreateNewProduct);
            ProductEntity savedProduct = productRepository.save(dataNewProduct);
            return productMapper.toProductDTO(savedProduct);
        } catch (Exception e) {
            log.error("Lỗi khi tạo sản phẩm mới: {}", e.getMessage(), e);
            throw new RuntimeException("Không thể tạo sản phẩm mới, vui lòng thử lại sau");
        }
    }

    public Page<ProductDTO> getListProduct(int page, int size, Double minPrice, Double maxPrice, Integer rating, String categoryId, String nameProduct, String sort) {
        try {
            Pageable pageable = PageRequest.of(page-1, size);
            Page<ProductEntity> products = productRepository.getAllProductByCondition(minPrice, maxPrice, nameProduct, rating, categoryId, pageable);
            Page<ProductDTO> productDTOPage = products.map(productMapper::toProductDTO);
            return productDTOPage;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> updateInfoProduct(String idProduct, RequestCreateProduct productUpdate ) {
        try {
            ProductEntity product = productRepository.findById(idProduct).orElseThrow(()-> new ProductNotFoundException("Product not found"));
            product.setPrice(productUpdate.getPrice());
            product.setQuantity(productUpdate.getQuantity());
            product.setDescription(productUpdate.getDescription());
            productRepository.save(product);
            return new ResponseEntity<>("Update produce successfully", HttpStatus.OK);
        }catch (Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    @Transactional
    public ResponseEntity<?> deleteProduct(String idProduce) {
        try{
            if(!productRepository.existsById(idProduce)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            productRepository.deleteById(idProduce);
            return new ResponseEntity<>("Delete produce successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ProductDTO getDataProduct(String idProduce) {
        try {
            log.info("get data product by idProduce {}", idProduce);
                ProductEntity product = productRepository.findById(idProduce).orElseThrow(()-> new ProductNotFoundException("Product not found"));
                ProductDTO produceDTO = productMapper.toProductDTO(product);
                productRepository.updateView(idProduce);
                List<ImageResponseDTO> listImage = imageRepository.findAllByProductAndDefaultImage(product, false).stream().map((imageMapper::toImageResponseDTO)).toList();
                List<String> images = listImage.stream().map(ImageResponseDTO::getPathImage).toList();
                produceDTO.setImages(images);
                return produceDTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "products", key = "#page + '-' + #size")
    public ResponseEntity<Page<ProductEntity>> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(productRepository.findAll(pageable), HttpStatus.OK);
    }



    @Override
    @SneakyThrows
    public String uploadImageForProduct(MultipartFile file, String id, boolean isDefault){
        ProductEntity product = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found"));
        if(isDefault){
            Image image = imageRepository.findByProductAndDefaultImage(product, true).orElse(null);
            if(image != null){
                isDefault = false;
            }
        }


        ImageServiceImpl.UploadFileDTO uploadFileDTO = imageService.uploadFile(file);
        Image image = new Image();
        image.setProduct(product);
        image.setPathImage(uploadFileDTO.getFile());
        image.setDescription("anh san pham");
        image.setDefaultImage(isDefault);
        if(isDefault){
            ProductEntity productEntity = productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found"));
            productEntity.setImage(uploadFileDTO.getFile());
        }

        Image image1 = imageRepository.save(image);
        if(isDefault) {
            product.setImage(image1.getPathImage());
        }
        return "SUCCESS";
    }
    @Override
    @SneakyThrows
    public Category createCategory(CategoryRequest categoryRequest){
        Category category = new Category();
        category.setName(categoryRequest.getName());
        category = categoryRepository.save(category);
        return category;
    }
    @Override
    @SneakyThrows
    public List<ProductDTO> getListProductByCategory(String categoryID){
        Category category = categoryRepository.findById(categoryID).orElseThrow(()->new RuntimeException("Category not found"));
        return productRepository.findAllByCategory(category).stream().map(productMapper::toProductDTO).collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDTO> getListCategory() {
        return categoryRepository.findAll().stream().map(categoryMapper::categoryToCategoryResponseDTO).toList();
    }

    @Override
    public List<ProductDTO> getAllProduct() {
        return productRepository.findAll().stream().map(productMapper::toProductDTO).toList();
    }
}