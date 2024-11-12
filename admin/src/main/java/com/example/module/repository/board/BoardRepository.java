package com.example.module.repository.board;

import com.example.module.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

}