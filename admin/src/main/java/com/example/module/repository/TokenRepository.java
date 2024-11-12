package com.example.module.repository;


import com.example.module.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long>, JpaSpecificationExecutor<Token> {
    Optional<Token> findFirstByTokenKeyOrderByCreatedAtDesc(String tokenKey);

    List<Token> findByTokenKey(String tokenKey);

    List<Token> findByTokenKeyAndDeletedFalse(String tokenKey);

    long deleteByTokenKey(String tokenKey);

}
