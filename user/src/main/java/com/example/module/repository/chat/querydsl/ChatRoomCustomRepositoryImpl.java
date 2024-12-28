package com.example.module.repository.chat.querydsl;

import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import com.example.module.util.SecurityContextHelper;
import com.example.module.util._Enum.ChatRoomCategory;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

import static com.example.module.entity.QChatMessage.chatMessage;
import static com.example.module.entity.QChatRoom.chatRoom;
import static com.example.module.entity.QChatRoomMember.chatRoomMember;
import static com.example.module.entity.QMember.member;
import static com.example.module.util.querydsl.QueryDslUtils.filterSetting;

@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseChatRoomDto> getChatRoomList(Map<String, Object> filters, Pageable pageable) {
        filterSetting(filters);

        // 서브쿼리: PRIVATE 채팅방인 경우 참여자 이름들을 가져오는 부분
        SubQueryExpression<String> groupConcatNames = JPAExpressions
                .select(Expressions.stringTemplate("group_concat({0})", member.name))
                .from(chatRoomMember)
                .leftJoin(member).on(chatRoomMember.subScriber.eq(member))
                .where(chatRoomMember.chatRoom.eq(chatRoom)
                        .and(chatRoomMember.subScriber.ne(SecurityContextHelper.getPrincipal()))); // 본인 계정을 제외한 나머지

        List<ResponseChatRoomDto> list = jpaQueryFactory
                .select(Projections.constructor(
                        ResponseChatRoomDto.class,
                        chatRoom.id,
                        new CaseBuilder()
                                .when(chatRoom.chatRoomCategory.eq(ChatRoomCategory.PRIVATE))
                                .then(groupConcatNames) // PRIVATE이면 참여자 이름들
                                .otherwise(chatRoom.title), // OPEN이면 채팅방의 원래 제목,
                        chatRoom.chatRoomCategory,
                        Expressions.stringTemplate("ifnull(DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s'), DATE_FORMAT({1}, '%Y-%m-%d %H:%i:%s'))",
                                chatMessage.createdAt.max(), chatRoom.createdAt)// 메시지가 없을시 채팅방 생성날짜를 기준으로 표시
                ))
                .from(chatRoom)
                .where(whereClause(filters))
                .join(chatRoomMember)
                .on(chatRoom.eq(chatRoomMember.chatRoom))
                .leftJoin(chatMessage)
                .on(chatRoom.eq(chatMessage.chatRoom))
                .groupBy(chatRoom.id, chatRoom.title, chatRoom.chatRoomCategory)
                .orderBy(new OrderSpecifier<>(Order.DESC, chatMessage.createdAt.max()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .stream()
                .toList();

        Long count = jpaQueryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .where(whereClause(filters))
                .join(chatRoomMember)
                .on(chatRoom.eq(chatRoomMember.chatRoom))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    private Predicate whereClause(Map<String, Object> filters) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chatRoom.deleted.isFalse());
        builder.and(chatRoomMember.subScriber.eq(SecurityContextHelper.getPrincipal()));

//        for (String s : filters.keySet()) {
//            switch(s){
////                case "title":
////                    builder.and(board.title.contains((String)filters.get(s)));
////                    break;
////                case "author":
////                    builder.and(board.createdMember.userId.eq((String)filters.get(s)));
////                default:
//            }
//        }
        return builder;
    }
}
