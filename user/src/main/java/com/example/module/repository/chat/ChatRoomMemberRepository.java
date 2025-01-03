package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import com.example.module.entity.ChatRoomMember;
import com.example.module.entity.Member;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChatRoomMemberRepository extends ChatRoomMemberCoreRepository {

    boolean existsByChatRoomAndSubScriber(ChatRoom chatRoom, Member subScriber);

    @Modifying
    @Query("update ChatRoomMember crm set crm.deleted = true where crm.chatRoom = :chatRoom")
    void markRoomMembersAsDeleted(@Param("chatRoom") ChatRoom chatRoom);

    Optional<ChatRoomMember> findByChatRoomAndCreatedMember(ChatRoom chatRoom, Member createdMember);


}