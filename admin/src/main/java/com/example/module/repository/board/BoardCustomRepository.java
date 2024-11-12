package com.example.module.repository.board;

import com.example.module.api.board.dto.response.ResponseBoardDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface BoardCustomRepository{
    Page<ResponseBoardDto> getBoardList(Map<String, Object> filters, Pageable pageable);
}