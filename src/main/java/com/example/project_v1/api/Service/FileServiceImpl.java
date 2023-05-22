package com.example.project_v1.api.Service;

import com.example.project_v1.db.repository.FileDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.project_v1.db.model.FileData;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService{

    private final FileDataRepository fileDataRepository;

    @Autowired
    public FileServiceImpl(FileDataRepository fileDataRepository) {
        this.fileDataRepository = fileDataRepository;
    }

    // 파일 업로드
    @Override
    public void uploadFile(List<MultipartFile> files) {

        String savePath = System.getProperty("user.dir") + "\\files";
        if (!new File(savePath).exists()) {
            try{
                new File(savePath).mkdir();
            } catch(Exception e){
                e.getStackTrace();
            }
        }

        try {
            for (MultipartFile file : files) {
                String origFilename = file.getOriginalFilename();
                UUID uuid = UUID.randomUUID();
                String filename = origFilename + uuid + ".png";

                String filePath = savePath + "\\" + filename;
                file.transferTo(new File(filePath));

                FileData fileData = FileData.builder()
                        .originFilename(origFilename)
                        .filename(filename)
                        .filePath(filePath)
                        .build();

                fileDataRepository.save(fileData);
            }
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace();
        }

    }

    // 파일 다운로드
    @Override
    public ResponseEntity<?> downloadFile(Long fileNum) throws Exception {

        FileData fileData = fileDataRepository.findByNum(fileNum)
                .orElseThrow(() -> new Exception("data is null"));

        String originName = fileData.getOriginFilename();
        String filename = fileData.getFilename();

        String encode = UriUtils.encode(originName, StandardCharsets.UTF_8);

        String contentDisposition = "attachment; filename=\\" + encode + "\\";
        UrlResource resource = new UrlResource("file:" + "C:\\YMJ\\study\\project_v1\\files\\"+filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

}
