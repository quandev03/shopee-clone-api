package com.example.banhangapi.api.service;

import com.example.banhangapi.api.service.implement.ImageServiceImpl;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    void getImageReadySave(MultipartFile file);
    ImageServiceImpl.UploadFileDTO uploadFile(MultipartFile file);
}
