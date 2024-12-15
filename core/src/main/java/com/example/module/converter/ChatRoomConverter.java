package com.example.module.converter;

import com.example.module.entity.Board;
import com.example.module.entity.ChatRoom;
import com.example.module.repository.chat.ChatRoomCoreRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class ChatRoomConverter implements
        Converter<String, ChatRoom>,
        com.fasterxml.jackson.databind.util.Converter<String, ChatRoom> {
    private final ChatRoomCoreRepository repository;

    public ChatRoomConverter(@Qualifier("chatRoomCoreRepository") ChatRoomCoreRepository repository) {
        this.repository = repository;
    }

    @Override
    public ChatRoom convert(String id) {
        return repository.findById(Long.parseLong(id)).orElseThrow(() -> new CommonException(ErrorCode.ACCESS_DENIED));
    }

    @Override
    public JavaType getInputType(TypeFactory typeFactory) {
        return typeFactory.constructType(String.class);
    }

    @Override
    public JavaType getOutputType(TypeFactory typeFactory) {
        return typeFactory.constructType(Board.class);
    }
}
