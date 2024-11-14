package com.example.module.repository.board.repository.board;

import com.example.module.entity.Board;
import com.example.module.entity.BoardComment;
import com.example.module.repository.BoardCommentCoreRepository;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends BoardCommentCoreRepository,BoardCommentCustomRepository{

    Optional<BoardComment> findFirstByBoardAndBoard_DeletedFalseAndBoardCommentAndDeletedFalseOrderBySortDesc(Board board, BoardComment boardComment);

    List<BoardComment> findByBoardAndBoardCommentNullOrderBySortAsc(Board board);
}