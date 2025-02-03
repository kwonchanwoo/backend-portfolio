package com.example.module.repository.chat.querydsl;

import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import com.example.module.entity.QChatMessage;
import com.example.module.entity.QChatRoomMember;
import com.example.module.util.SecurityContextHelper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.DateTimeExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.module.entity.QChatMessage.chatMessage;
import static com.example.module.entity.QChatMessageRead.chatMessageRead;
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

        // 1. 서브쿼리: PRIVATE 채팅방 제목이 Null인경우 참여자 이름을 가져오는 서브쿼리
        SubQueryExpression<String> groupConcatNames = JPAExpressions
                .select(Expressions.stringTemplate("group_concat_custom({0}, {1})", member.name,Expressions.constant(", ")))
                .from(chatRoomMember)
                .leftJoin(member).on(chatRoomMember.subScriber.eq(member))
                .where(chatRoomMember.chatRoom.eq(chatRoom)
                        .and(chatRoomMember.subScriber.ne(SecurityContextHelper.getPrincipal()))); // 본인 계정을 제외한 나머지

        // 2. 서브쿼리: 채팅방 회원 목록 수
        QChatRoomMember qChatRoomMember = new QChatRoomMember("sub_chat_room_member");

        SubQueryExpression<Long> memberCount = JPAExpressions
                .select(qChatRoomMember.count())
                .from(qChatRoomMember)
                .where(qChatRoomMember.chatRoom.eq(chatRoom));

        // 3. 서브쿼리: 최근 메시지 (1개)
        QChatMessage qChatMessage = new QChatMessage("sub_chat_message");
        SubQueryExpression<String> recentMessage = JPAExpressions
                .select(qChatMessage.contents)
                .from(qChatMessage)
                .where(qChatMessage.chatRoom.eq(chatRoom).and(qChatMessage.id.eq(chatMessage.id.max())))
                .orderBy(new OrderSpecifier<>(Order.DESC, qChatMessage.createdAt));

        DateTimeExpression<LocalDateTime> ifnullDateTimeExpression = Expressions.dateTimeTemplate(
                java.time.LocalDateTime.class,
                "ifnull({0}, {1})",
                chatRoomMember.updatedAt, chatRoomMember.createdAt
        );

        List<ResponseChatRoomDto> list = jpaQueryFactory
                .select(Projections.constructor(
                        ResponseChatRoomDto.class,
                        chatRoom.id,
                        new CaseBuilder()
                                .when(chatRoom.title.isNull())
                                .then(groupConcatNames) // PRIVATE이면 참여자 이름들
                                .otherwise(chatRoom.title), // OPEN이면 채팅방의 원래 제목,
                        chatRoom.chatRoomCategory, // 채팅방 종류 (개인, 오픈)
                        Expressions.stringTemplate("ifnull(DATE_FORMAT({0}, '%Y-%m-%d %H:%i:%s'), DATE_FORMAT({1}, '%Y-%m-%d %H:%i:%s'))",
                                chatMessage.createdAt.max(), chatRoom.createdAt), // 메시지가 없을시 채팅방 생성날짜를 기준으로 표시
                        chatMessage.count().subtract(chatMessageRead.count()),
                        memberCount, // 채팅방 회원 수
                        recentMessage // 최근 메시지
                ))
                .from(chatRoom)
                .where(whereClause(filters))
                .join(chatRoomMember)
                .on(chatRoom.eq(chatRoomMember.chatRoom))
                .leftJoin(chatMessage)
                .on(chatRoom.eq(chatMessage.chatRoom).and(chatMessage.createdAt.goe(ifnullDateTimeExpression)))
                .leftJoin(chatMessageRead)
                .on(chatMessageRead.chatMessage.eq(chatMessage).and(chatMessageRead.createdMember.eq(SecurityContextHelper.getPrincipal())))
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
