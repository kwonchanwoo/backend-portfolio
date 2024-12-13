package com.example.module.repository.file.querydsl;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryDto;
import com.example.module.util.SecurityContextHelper;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import static com.example.module.entity.QFileCategory.fileCategory;
import static com.example.module.entity.QFileCategoryRole.fileCategoryRole;

@RequiredArgsConstructor
public class FileCategoryCustomRepositoryImpl implements FileCategoryCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<ResponseFileCategoryDto> getFileCategoryList() {
        List<ResponseFileCategoryDto> fileCategoryDtoList = new ArrayList<>();

        if (!SecurityContextHelper.isAdmin()) { // 일반회원
            fileCategoryDtoList = jpaQueryFactory.
                    select(Projections.constructor(
                            ResponseFileCategoryDto.class,
                            fileCategory.id,
                            fileCategory.name
                    ))
                    .from(fileCategory)
                    .leftJoin(fileCategoryRole)
                    .on(
                            fileCategoryRole.fileCategoryRolePK.member.id.eq(SecurityContextHelper.getPrincipal().getId()),
                            fileCategoryRole.fileCategoryRolePK.fileCategory.id.eq(fileCategory.id)
                    )
                    .where(fileCategoryRole.deleted.isFalse().and(fileCategory.deleted.isFalse()))
                    .fetch();
        } else { // 관리자
            fileCategoryDtoList = jpaQueryFactory.
                    select(Projections.constructor(
                            ResponseFileCategoryDto.class,
                            fileCategory.id,
                            fileCategory.name
                    ))
                    .from(fileCategory)
                    .where(fileCategory.deleted.isFalse())
                    .fetch();
        }
        return fileCategoryDtoList;
    }
}

