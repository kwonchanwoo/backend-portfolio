package com.example.module.repository;

import com.example.module.dto.FileCategoryRolePK;
import com.example.module.entity.FileCategoryRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileCategoryRoleCoreRepository extends JpaRepository<FileCategoryRole, FileCategoryRolePK> {
}