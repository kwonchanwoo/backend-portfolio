package com.example.module.repository.board;

import com.example.module.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentCoreRepository extends JpaRepository<BoardComment,Long> {
}