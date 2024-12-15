package com.example.module.repository.chat;

import com.example.module.entity.ChatRoom;
import com.example.module.entity.Member;

public interface ChatRoomMemberRepository extends ChatRoomMemberCoreRepository {

    boolean existsByChatRoomAndCreatedMember(ChatRoom chatRoom, Member createdMember);

    boolean existsByChatRoomAndSubScriber(ChatRoom chatRoom, Member subScriber);

}