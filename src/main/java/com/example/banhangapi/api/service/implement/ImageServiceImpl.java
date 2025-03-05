package com.example.banhangapi.api.service.implement;

import com.example.banhangapi.api.service.ImageService;
import com.example.banhangapi.config.AppConfig;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;
import java.security.SecureRandom;
import java.util.UUID;
@Service
@Slf4j
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final AppConfig appConfig;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final SecureRandom random = new SecureRandom();
    public void getImageReadySave(MultipartFile file){}
    @Override
    @SneakyThrows
    public UploadFileDTO uploadFile(MultipartFile file)  {
        Bucket bucket = StorageClient.getInstance().bucket();
        String fileName = UUID.randomUUID().toString();
        Blob blob = bucket.create(fileName, file.getBytes(), file.getContentType());
        return new UploadFileDTO(fileName, appConfig.getUrlFirebase() + appConfig.getBucketName() + "/o/"
                + blob.getName().replaceAll("/", "%2F") + "?alt=media");
    }
    @Data
    @AllArgsConstructor
    public class UploadFileDTO{
        private String fileName;
        private String file;
    }

}
