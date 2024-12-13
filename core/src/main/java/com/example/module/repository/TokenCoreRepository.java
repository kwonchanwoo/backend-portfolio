package com.example.module.repository;


import com.example.module.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenCoreRepository extends JpaRepository<Token, Long>{
    Optional<Token> findFirstByTokenKeyOrderByCreatedAtDesc(String tokenKey);

    List<Token> findByTokenKey(String tokenKey);

    List<Token> findByTokenKeyAndDeletedFalse(String tokenKey);

    long deleteByTokenKey(String tokenKey);

}
