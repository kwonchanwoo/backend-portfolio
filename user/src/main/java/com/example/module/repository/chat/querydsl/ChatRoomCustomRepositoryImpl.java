package com.example.module.repository.chat.querydsl;

import com.example.module.api.chat.dto.response.ResponseChatRoomDto;
import com.example.module.entity.QChatMessage;
import com.example.module.entity.QChatRoomMember;
import com.example.module.entity.QMember;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static com.example.module.entity.QChatMessage.chatMessage;
import static com.example.module.entity.QChatMessageRead.chatMessageRead;
import static com.example.module.entity.QChatRoom.chatRoom;
import static com.example.module.entity.QChatRoomCustomTitle.chatRoomCustomTitle;
import static com.example.module.entity.QChatRoomMember.chatRoomMember;
import static com.example.module.entity.QMember.member;
import static com.example.module.util.querydsl.QueryDslUtils.filterSetting;

@RequiredArgsConstructor
public class ChatRoomCustomRepositoryImpl implements ChatRoomCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<ResponseChatRoomDto> getChatRoomList(Map<String, Object> filters, Pageable pageable) {
        filterSetting(filters);

        QChatMessage chatMessage2 = new QChatMessage("sub_chat_message2");
        QChatMessage chatMessage3 = new QChatMessage("sub_chat_message3");

        List<ResponseChatRoomDto> list = jpaQueryFactory
                .select(Projections.constructor(
                        ResponseChatRoomDto.class,
                        chatRoom.id,
                        new CaseBuilder()
                                .when(chatRoomCustomTitle.customTitle.isNotNull())
                                .then(chatRoomCustomTitle.customTitle)
                                .when(chatRoom.chatRoomCategory.eq(ChatRoomCategory.OPEN))
                                .then(chatRoom.title)
                                .otherwise(groupConcatNames()), // subQuery 접속회원 ex)A, B, C,
                        chatRoom.chatRoomCategory, // 채팅방 종류 (개인, 오픈)
                        Expressions.stringTemplate("DATE_FORMAT(ifnull({0},{1}),'%Y-%m-%d %H:%i:%s')",
                                chatMessage.createdAt.max(), chatRoom.createdAt), // 메시지가 없을시 채팅방 생성날짜를 기준으로 표시
                        new CaseBuilder().when(chatMessageRead.id.isNull()).then(1L).otherwise(0L).sum(),
                        memberCount(), // subQuery 채팅방 회원 수
                        chatMessage2.contents // 최근 메시지
                ))
                .from(chatRoom)
                .join(chatRoomMember)
                .on(chatRoomMember.deleted.isFalse().and(chatRoom.eq(chatRoomMember.chatRoom)))
                .leftJoin(chatMessage)
                .on(chatMessage.deleted.isFalse()
                        .and(chatRoom.eq(chatMessage.chatRoom))
                        .and(chatMessage.createdAt.goe(Expressions.dateTimeTemplate(
                                LocalDateTime.class,
                                "ifnull({0}, {1})",
                                chatRoomMember.updatedAt, chatRoomMember.createdAt
                        ))))
                .leftJoin(chatMessageRead)
                .on(chatMessageRead.deleted.isFalse()
                        .and(chatMessageRead.chatMessage.eq(chatMessage))
                        .and(chatMessageRead.createdMember.eq(SecurityContextHelper.getPrincipal())))
                .leftJoin(chatMessage2)
                .on(chatMessage2.deleted.isFalse()
                        .and(chatMessage2.chatRoom.eq(chatRoom))
                        .and(chatMessage2.createdAt.eq(JPAExpressions.select(chatMessage3.createdAt.max())
                                .from(chatMessage3)
                                .where(chatMessage3.deleted.isFalse().and(chatMessage3.chatRoom.eq(chatRoom)))
                        )))
                .leftJoin(chatRoomCustomTitle) // 사용자별 제목 설정 확인
                .on(chatRoomCustomTitle.deleted.isFalse()
                        .and(chatRoomCustomTitle.chatRoomCustomTitlePK.chatRoom.eq(chatRoom))
                        .and(chatRoomCustomTitle.chatRoomCustomTitlePK.member.eq(SecurityContextHelper.getPrincipal())))
                .where(whereClause(filters))
                .groupBy(chatRoom.id, chatRoom.title, chatRoom.chatRoomCategory, chatMessage2.contents)
                .orderBy(new OrderSpecifier<>(Order.DESC, chatMessage.createdAt.max()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = jpaQueryFactory
                .select(chatRoom.count())
                .from(chatRoom)
                .join(chatRoomMember)
                .on(chatRoomMember.deleted.isFalse().and(chatRoom.eq(chatRoomMember.chatRoom)))
                .leftJoin(chatRoomCustomTitle) // 사용자별 제목 설정 확인
                .on(chatRoomCustomTitle.deleted.isFalse()
                        .and(chatRoomCustomTitle.chatRoomCustomTitlePK.chatRoom.eq(chatRoom))
                        .and(chatRoomCustomTitle.chatRoomCustomTitlePK.member.eq(SecurityContextHelper.getPrincipal())))
                .where(whereClause(filters))
                .fetchOne();

        return new PageImpl<>(list, pageable, count);
    }

    private Predicate whereClause(Map<String, Object> filters) {
        BooleanBuilder builder = new BooleanBuilder();
        builder.and(chatRoom.deleted.isFalse());
        builder.and(chatRoomMember.subScriber.eq(SecurityContextHelper.getPrincipal()));

        filters.forEach((key, value) -> {
            switch (key) {
                case "title" -> {
                    QChatRoomMember subChatRoomMember3 = new QChatRoomMember("sub_chat_room_member3");
                    QMember subMember2 = new QMember("sub_member2");

                     BooleanBuilder condition1 = new BooleanBuilder();
                     condition1.and(chatRoomCustomTitle.customTitle.isNotNull())
                             .and(chatRoomCustomTitle.customTitle.contains((String)value));

                     BooleanBuilder condition2 = new BooleanBuilder();
                     condition2.and(chatRoomCustomTitle.customTitle.isNull())
                             .and(chatRoom.chatRoomCategory.eq(ChatRoomCategory.OPEN))
                             .and(chatRoom.title.contains((String)value));

                     BooleanBuilder condition3 = new BooleanBuilder();
                     condition3.and(chatRoomCustomTitle.customTitle.isNull()
                             .and(JPAExpressions
                                     .select(subChatRoomMember3.id)
                                     .from(subChatRoomMember3)
                                     .leftJoin(subMember2).on(subChatRoomMember3.subScriber.eq(subMember2))
                                     .where(subChatRoomMember3.chatRoom.eq(chatRoom)
                                             .and(subMember2.name.contains((String)value)))
                                     .exists()));

                    builder.and(condition1.or(condition2).or(condition3));
                }
            }
        });
        return builder;
    }

    // 1. 서브쿼리: PRIVATE 채팅방 제목이 Null인경우 참여자 이름을 가져오는 서브쿼리
    private SubQueryExpression<String> groupConcatNames() {
        QChatRoomMember subChatRoomMember1 = new QChatRoomMember("sub_chat_room_member1");
        return JPAExpressions
                .select(Expressions.stringTemplate("group_concat_custom({0}, {1})", member.name, Expressions.constant(", ")))
                .from(subChatRoomMember1)
                .leftJoin(member).on(member.deleted.isFalse()
                        .and(subChatRoomMember1.subScriber.eq(member)))
                .where(subChatRoomMember1.deleted.isFalse()
                        .and(subChatRoomMember1.chatRoom.eq(chatRoom))
                        .and(subChatRoomMember1.subScriber.ne(SecurityContextHelper.getPrincipal())));
    }

    // 2. 서브쿼리: 채팅방 회원 목록 수
    private SubQueryExpression<Long> memberCount() {
        QChatRoomMember subChatRoomMember2 = new QChatRoomMember("sub_chat_room_member2");
        return JPAExpressions
                .select(subChatRoomMember2.count())
                .from(subChatRoomMember2)
                .where(subChatRoomMember2.deleted.isFalse().and(subChatRoomMember2.chatRoom.eq(chatRoom)));
    }
}
