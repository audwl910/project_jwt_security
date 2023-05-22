package com.example.project_v1.api.controller;

import com.example.project_v1.api.Service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/file")
public class FileController {
    
    private final FileService fileService;

    @Autowired
    public FileController(FileService fileService) {
        this.fileService = fileService;
    }
    
    // 파일 업로드
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("files") List<MultipartFile> files){
        fileService.uploadFile(files);
        return ResponseEntity.status(200).body("파일 업로드에 성공하셨습니다.");
    }

    // 파일 다운로드
    @GetMapping("/download/{fileNum}")
    public ResponseEntity<?> downloadFile(@PathVariable("fileNum") Long fileNum) throws Exception {
        return fileService.downloadFile(fileNum);
    }
    
}
