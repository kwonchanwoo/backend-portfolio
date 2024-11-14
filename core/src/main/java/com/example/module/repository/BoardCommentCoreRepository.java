package com.example.module.repository;

import com.example.module.entity.BoardComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCommentCoreRepository extends JpaRepository<BoardComment,Long> {
}