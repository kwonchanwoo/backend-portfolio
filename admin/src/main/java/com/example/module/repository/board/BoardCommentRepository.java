package com.example.module.repository.board;

import com.example.module.entity.Board;
import com.example.module.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long>,BoardCommentCustomRepository{

    Optional<BoardComment> findFirstByBoardAndBoard_DeletedFalseAndBoardCommentAndDeletedFalseOrderBySortDesc(Board board, BoardComment boardComment);

    List<BoardComment> findByBoardAndBoardCommentNullOrderBySortAsc(Board board);
}