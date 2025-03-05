package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.dto.ImageResponseDTO;
import com.example.banhangapi.api.dto.ProductDTO;
import com.example.banhangapi.api.entity.Image;
import com.example.banhangapi.api.entity.ProductEntity;
import com.example.banhangapi.api.mapper.ImageMapper;
import com.example.banhangapi.api.mapper.ProductMapper;
import com.example.banhangapi.api.repository.ImageRepository;
import com.example.banhangapi.api.repository.ProductRepository;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.banhangapi.api.service.ImageService;
import com.example.banhangapi.api.service.ProductService;
import com.example.banhangapi.grpc.GrpcClientService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.kafka.MessageProducer;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    final MessageProducer messageProducer;

    final GrpcClientService grpcClientService;

    final ProductMapper productMapper;

    final ImageService imageService;
    final ImageMapper imageMapper;
    private final ImageRepository imageRepository;

    @Transactional
    public ResponseEntity<?> createNewProduct(RequestCreateProduct dataCreateNewProduct) {
       try{
           if (productRepository.existsByNameProduct(dataCreateNewProduct.getNameProduct())) {
               throw new RuntimeException("Loi");
           }
           ProductEntity dataNewProduct = productMapper.ReqToProduct(dataCreateNewProduct);
           ProductEntity product = this.productRepository.save(dataNewProduct);
           messageProducer.sendMessage("create_product", dataNewProduct.getId().toString());
           return new ResponseEntity<>(dataNewProduct, HttpStatus.CREATED);
       }catch (Exception e){
           throw new RuntimeException();
       }
    }

    public Page<ProductDTO> getListProduct(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<ProductEntity> products = productRepository.findAll(pageable);
            Page<ProductDTO> productDTOs = new PageImpl<>(
                    products.stream()
                            .map(product -> productMapper.toProductDTO(product))
                            .collect(Collectors.toList()),
                    pageable,
                    size
            );
            return productDTOs;
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
            messageProducer.sendMessage("delete_produce", idProduce);
            return new ResponseEntity<>("Delete produce successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ProductDTO getDataProduct(String idProduce) {
        try {
            messageProducer.sendMessage("product-view", idProduce);
                ProductEntity product = productRepository.findById(idProduce).orElseThrow(()-> new ProductNotFoundException("Product not found"));
                ProductDTO produceDTO = productMapper.toProductDTO(product);
                List<ImageResponseDTO> listImage = imageRepository.findAllByProduct(product).stream().map((imageMapper::toImageResponseDTO)).toList();
                produceDTO.setImage(listImage);
                return produceDTO;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "products", key = "#page + '-' + #size")
    public ResponseEntity<Page<ProductEntity>> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(productRepository.findAll(pageable), HttpStatus.OK);
    }


    public List<ProductEntity> searchProduct(RequestSearchProduct requestSearchProduct) {
        List<ProductEntity> productList = List.of();
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            String requestJson = objectMapper.writeValueAsString(requestSearchProduct);
            productList = grpcClientService.searchProduct(requestJson);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return productList;
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
        imageRepository.save(image);
        return "SUCCESS";
    }
}