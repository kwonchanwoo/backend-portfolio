package com.example.module.repository.file;

import com.example.module.entity.FileCategory;
import com.example.module.repository.file.querydsl.FileCategoryCustomRepository;

import java.util.Optional;

public interface FileCategoryRepository extends FileCategoryCoreRepository, FileCategoryCustomRepository {
    Optional<FileCategory> findByName(String name);

    Optional<FileCategory> findByNameAndDeletedFalse(String name);

    Optional<FileCategory> findByIdAndDeletedFalse(Long id);

}