package com.example.module.repository;

import com.example.module.dto.FileCategoryRolePK;
import com.example.module.entity.FileCategoryRole;
import com.example.module.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileCategoryRoleRepository extends JpaRepository<FileCategoryRole, FileCategoryRolePK> {

    List<FileCategoryRole> findByFileCategoryRolePK_Member(Member member);
}