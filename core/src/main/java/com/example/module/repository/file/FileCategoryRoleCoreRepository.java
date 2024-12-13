package com.example.module.repository.file;

import com.example.module.dto.FileCategoryRolePK;
import com.example.module.entity.FileCategoryRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileCategoryRoleCoreRepository extends JpaRepository<FileCategoryRole, FileCategoryRolePK> {
}