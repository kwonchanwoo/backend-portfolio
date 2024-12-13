package com.example.module.repository.board.querydsl;

import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.entity.Board;

import java.util.List;

public interface BoardCommentCustomRepository {
    List<BoardCommentDto> getBoardCommentList(Board board);
}
