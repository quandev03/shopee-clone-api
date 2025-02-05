package com.example.banhangapi.api.service;

import com.example.banhangapi.api.entity.Product;
import com.example.banhangapi.api.entity.ProductDTO;
import com.example.banhangapi.api.mapper.ProductMapper;
import com.example.banhangapi.api.repository.ProductRepository;
import com.example.banhangapi.api.request.RequestCreateProduct;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.banhangapi.grpc.GrpcClientService;
import com.example.banhangapi.helper.handleException.ProductNotFoundException;
import com.example.banhangapi.kafka.MessageProducer;
import com.example.banhangapi.redis.RedisServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductService {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    RedisServiceImpl redisService;

    @Autowired
    MessageProducer messageProducer;

    @Autowired
    GrpcClientService grpcClientService;

    @Autowired
    ProductMapper productMapper;

    @Transactional
    public ResponseEntity<?> save(RequestCreateProduct dataCreateNewProduct) {
       try{
           if (productRepository.existsByNameProduct(dataCreateNewProduct.getNameProduct())) {
               return new ResponseEntity<>("Name product already exist", HttpStatus.CONFLICT);
           }
           Product dataNewProduct = productMapper.ReqToProduct(dataCreateNewProduct);
           this.productRepository.save(dataNewProduct);
           messageProducer.sendMessage("create_product", dataNewProduct.getId().toString());
           return new ResponseEntity<>(dataNewProduct, HttpStatus.CREATED);
       }catch (Exception e){
           throw new RuntimeException();
       }
    }

    public Page<Product> getAll(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            return productRepository.findAll( pageable);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ResponseEntity<?> updateProduct(RequestCreateProduct productUpdate, Long idProduct ) {
        try {
            Product product = productRepository.findById(idProduct).orElseThrow(()-> new ProductNotFoundException("Product not found"));
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
    public ResponseEntity<?> deleteProduce(Long idProduce) {
        try{
            if(!productRepository.existsById(idProduce)){
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            productRepository.deleteById(idProduce);
            messageProducer.sendMessage("delete_produce", idProduce.toString());
            return new ResponseEntity<>("Delete produce successfully", HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public ProductDTO getDataProduceById(Long idProduce) {
        try {
            messageProducer.sendMessage("produce-view", idProduce.toString());
            String key = "data-product-" + idProduce;
            if (redisService.hasKey(key)) {
                return (ProductDTO) redisService.getData(key);
            }else {
                Product product = productRepository.findById(idProduce).orElseThrow(()-> new ProductNotFoundException("Product not found"));
                ProductDTO produceDTO = productMapper.toProductDTO(product);
                redisService.saveData(key, produceDTO, 300000);
                return produceDTO;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Cacheable(value = "products", key = "#page + '-' + #size")
    public ResponseEntity<Page<Product>> getProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return new ResponseEntity<>(productRepository.findAll(pageable), HttpStatus.OK);
    }


    public List<Product> searchProduct(RequestSearchProduct requestSearchProduct) {
        List<Product> productList = List.of();
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
}