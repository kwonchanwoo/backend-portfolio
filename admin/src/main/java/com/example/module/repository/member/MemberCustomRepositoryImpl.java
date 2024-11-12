package com.example.module.repository.member;

import com.example.module.api.file_category.dto.response.ResponseFileCategoryMemberDto;
import com.example.module.api.member.dto.response.ResponseMemberDto;
import com.example.module.entity.FileCategory;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.module.entity.QFileCategoryRole.fileCategoryRole;
import static com.example.module.entity.QMember.member;
import static com.example.module.util.querydsl.QueryDslUtils.filterSetting;

@RequiredArgsConstructor
public class MemberCustomRepositoryImpl implements MemberCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseMemberDto> getMemberList(Map<String, Object> filters, Pageable pageable) {

        filterSetting(filters);

        List<ResponseMemberDto> list = jpaQueryFactory.
                selectFrom(member)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                .stream()
                .map(ResponseMemberDto::new)
                .collect(Collectors.toList());

        Long count = Optional.ofNullable(jpaQueryFactory.
                select(member.count())
                .from(member)
                .fetchOne()).orElse(0L);

        return new PageImpl<>(list, pageable, count);
    }

    @Override
    public Page<ResponseFileCategoryMemberDto> getFileCategoryMemberList(FileCategory fileCategory, Map<String, Object> filters, Pageable pageable) {

        filterSetting(filters);

        BooleanPath permission_status = Expressions.booleanPath("permission_status"); // 서브쿼리를 정렬로 사용하기 위해서 추가

        List<ResponseFileCategoryMemberDto> list = jpaQueryFactory.select(Projections.constructor(
                        ResponseFileCategoryMemberDto.class,
                        member.id,
                        member.userId,
                        member.name,
                        member.email,
                        member.sex,
                        member.age,
                        Expressions.asBoolean(
                                JPAExpressions.selectOne()
                                        .from(fileCategoryRole)
                                        .where(fileCategoryRole.fileCategoryRolePK.fileCategory.id.eq(fileCategory.getId())
                                                .and(fileCategoryRole.fileCategoryRolePK.member.eq(member))
                                        ).exists().as(permission_status))
                ))
                .from(member)
                .orderBy(getOrderSpecifiers(pageable.getSort(),permission_status).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = Optional.ofNullable(jpaQueryFactory.select(member.count())
                .from(member)
                .fetchOne()).orElse(0L);


        return new PageImpl<>(list, pageable, count);
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort,BooleanPath permission_status) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

            if (property.equals("permission_status")) { // 서브쿼리를 정렬로 사용
                orders.add(new OrderSpecifier<>(direction, permission_status));
            } else {
                try {
                    member.getClass().getField(property);
                } catch (NoSuchFieldException e) {
                    throw new CommonException(ErrorCode.SORT_KEY_NOT_EXISTS);
                }

                // 일반적인 필드 정렬
                PathBuilder<Object> entityPath = new PathBuilder<>(member.getType(), member.getMetadata());
                orders.add(new OrderSpecifier(direction, entityPath.get(property)));
            }
        });

        return orders;
    }
}