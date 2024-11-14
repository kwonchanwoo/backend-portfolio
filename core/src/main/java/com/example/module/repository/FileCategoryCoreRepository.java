package com.example.module.repository;

import com.example.module.entity.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileCategoryCoreRepository extends JpaRepository<FileCategory, Long>{
}