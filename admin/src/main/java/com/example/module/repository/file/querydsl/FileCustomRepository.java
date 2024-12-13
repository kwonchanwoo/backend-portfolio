package com.example.module.repository.file.querydsl;

import com.example.module.api.file.dto.response.ResponseFileDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FileCustomRepository {
    Page<ResponseFileDto> getFileList(Map<String, Object> filters, Pageable pageable);
}
