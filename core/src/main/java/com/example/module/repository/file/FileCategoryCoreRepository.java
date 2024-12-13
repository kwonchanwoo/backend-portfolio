package com.example.module.repository.file;

import com.example.module.entity.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCategoryCoreRepository extends JpaRepository<FileCategory, Long>{
}