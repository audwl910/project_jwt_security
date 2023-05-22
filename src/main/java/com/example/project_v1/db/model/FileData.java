package com.example.project_v1.db.model;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FileData {

    @Id //primary key
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long num;
    private String originFilename;
    private String filename;
    private String filePath;

    @Builder
    public FileData(String originFilename, String filename, String filePath){
        this.originFilename = originFilename;
        this.filename = filename;
        this.filePath = filePath;
    }
}
