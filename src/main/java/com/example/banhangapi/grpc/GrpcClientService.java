package com.example.banhangapi.grpc;

import com.example.banhangapi.api.entity.Product;
import com.example.banhangapi.api.request.RequestSearch;
import com.example.banhangapi.api.request.RequestSearchProduct;
import com.example.grpc.SearchServiceGrpc;
import com.example.grpc.SearchServiceProto;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GrpcClientService {
    @Autowired
    ObjectMapper objectMapper;

    @GrpcClient("searchService")
    private SearchServiceGrpc.SearchServiceBlockingStub searchServiceStub;

    public List<Product> searchProduct(String query) {
        SearchServiceProto.SearchRequest request = SearchServiceProto.SearchRequest.newBuilder()
                .setQuery(query)
                .build();
        SearchServiceProto.SearchResponse response = searchServiceStub.searchProduct(request);
        String responseJson = response.getResults(0);
        List<Product> products;
        try {
            products = objectMapper.readValue(responseJson, new TypeReference<List<Product>>() {});
        }catch (Exception e){
            throw new RuntimeException();
        }
        return products;
    }

    public String searchUser(RequestSearch query) {
        SearchServiceProto.SearchRequest request = SearchServiceProto.SearchRequest.newBuilder()
                .setQuery(query.toString())
                .build();
        SearchServiceProto.SearchResponse response = searchServiceStub.searchUser(request);
        return String.join(", ", response.getResultsList());
    }

}