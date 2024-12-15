package com.example.module.entity;

import com.example.module.util.BaseEntity;
import com.example.module.util._Enum.ChatRoomCategory;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ChatRoom extends BaseEntity {
    private String title;
    @Enumerated(EnumType.STRING)
    private ChatRoomCategory chatRoomCategory;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<ChatMessage> chatMessages;

}
