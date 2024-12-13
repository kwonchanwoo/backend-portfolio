package com.example.module.repository.file.querydsl;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;

import java.util.List;

public interface FileCategoryCustomRepository {
    List<ResponseFileCategoryDto> getFileCategoryList();
}
