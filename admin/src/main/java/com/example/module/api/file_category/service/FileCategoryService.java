package com.example.module.api.file_category.service;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;
import com.example.module.repository.file_category.FileCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FileCategoryService {
    private final FileCategoryRepository fileCategoryRepository;

    public List<ResponseFileCategoryDto> getFileCategoryList() {
        return fileCategoryRepository.getFileCategoryList();
    }
}
