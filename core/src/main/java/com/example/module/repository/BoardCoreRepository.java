package com.example.module.repository;

import com.example.module.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardCoreRepository extends JpaRepository<Board, Long> {

}