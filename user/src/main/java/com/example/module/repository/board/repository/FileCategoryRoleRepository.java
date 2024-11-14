package com.example.module.repository.board.repository;

import com.example.module.entity.FileCategoryRole;
import com.example.module.entity.Member;
import com.example.module.repository.FileCategoryRoleCoreRepository;

import java.util.List;

public interface FileCategoryRoleRepository extends FileCategoryRoleCoreRepository {

    List<FileCategoryRole> findByFileCategoryRolePK_Member(Member member);
}