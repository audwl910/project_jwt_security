package com.example.project_v1.db.repository;


import com.example.project_v1.db.model.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Long> {

    Optional<FileData> findByNum(Long num);

}
