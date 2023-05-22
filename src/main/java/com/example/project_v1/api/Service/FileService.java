package com.example.project_v1.api.Service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    void uploadFile(List<MultipartFile> files);

    ResponseEntity<?> downloadFile(Long fileNum) throws Exception;
}
