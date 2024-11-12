package com.example.module.repository.board;

import com.example.module.api.board.dto.response.ResponseBoardDto;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.module.entity.QBoard.board;
import static com.example.module.entity.QBoardComment.boardComment1;
import static com.example.module.entity.QMember.member;
import static com.example.module.util.querydsl.QueryDslUtils.filterSetting;

@RequiredArgsConstructor
public class BoardCustomRepositoryImpl implements BoardCustomRepository{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseBoardDto> getBoardList(Map<String, Object> filters, Pageable pageable) {

        filterSetting(filters);

        List<ResponseBoardDto> list = jpaQueryFactory.select(Projections.constructor(
                        ResponseBoardDto.class,
                        board.id,
                        board.title,
                        board.contents,
                        board.views,
                        board.createdMember.userId,
                        Expressions.asNumber(
                                JPAExpressions.select(boardComment1.count())
                                        .from(boardComment1)
                                        .where(boardComment1.board.eq(board))
                        ).castToNum(Integer.class).coalesce(0).as("board_comment_count")
                ))
                .from(board)
                .where(whereClause(filters))
                .orderBy(getOrderSpecifiers(pageable.getSort()).toArray(new OrderSpecifier[0]))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory.select(board.count()).from(board).fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    private Predicate whereClause(Map<String, Object> filters) {
        BooleanBuilder builder = new BooleanBuilder();
        for (String s : filters.keySet()) {
            switch(s){
                case "title":
                    builder.and(board.title.contains((String)filters.get(s)));
                    break;
                case "author":
                    builder.and(board.createdMember.userId.eq((String)filters.get(s)));
                default:
            }
        }
        return builder;
    }

    private List<OrderSpecifier> getOrderSpecifiers(Sort sort) {
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();

                try {
                    member.getClass().getField(property);
                } catch (NoSuchFieldException e) {
                    throw new CommonException(ErrorCode.SORT_KEY_NOT_EXISTS);
                }

                // 일반적인 필드 정렬
                PathBuilder<Object> entityPath = new PathBuilder<>(member.getType(), member.getMetadata());
                orders.add(new OrderSpecifier(direction, entityPath.get(property)));
        });
        return orders;
    }
}
