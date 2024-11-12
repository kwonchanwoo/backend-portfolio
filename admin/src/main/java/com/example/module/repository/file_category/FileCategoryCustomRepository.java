package com.example.module.repository.file_category;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;

import java.util.List;

public interface FileCategoryCustomRepository {
    List<ResponseFileCategoryDto> getFileCategoryList();
}
