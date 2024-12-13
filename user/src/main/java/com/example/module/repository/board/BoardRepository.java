package com.example.module.repository.board;

import com.example.module.entity.Board;
import com.example.module.repository.board.querydsl.BoardCustomRepository;

import java.util.Optional;

public interface BoardRepository extends BoardCoreRepository, BoardCustomRepository {

    Optional<Board> findByIdAndDeletedFalse(Long id);
}