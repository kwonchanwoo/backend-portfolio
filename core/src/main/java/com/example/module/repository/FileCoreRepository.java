package com.example.module.repository;

import com.example.module.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCoreRepository extends JpaRepository<File, Long> {
}
