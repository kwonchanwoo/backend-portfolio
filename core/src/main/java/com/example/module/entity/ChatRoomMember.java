package com.example.module.entity;

import com.example.module.util.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Table(uniqueConstraints = @UniqueConstraint(name = "chat_room_unique",columnNames = {"chat_room_id","created_member_id"}))
public class ChatRoomMember extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

}