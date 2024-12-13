package com.example.module.repository.board.querydsl;

import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.entity.Board;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BoardCommentCustomRepositoryImpl implements BoardCommentCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<BoardCommentDto> getBoardCommentList(Board board) {
        return List.of();
    }
}
