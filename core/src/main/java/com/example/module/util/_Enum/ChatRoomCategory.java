package com.example.module.util._Enum;

import com.example.module.util.CommonException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.example.module.util._Enum.ErrorCode.ENUM_ROOM_CATEGORY_INVALID;

public enum ChatRoomCategory {
    PRIVATE,OPEN;

    @JsonCreator
    public static ChatRoomCategory fromValue(String value) {
        for (ChatRoomCategory chatRoomCategory : ChatRoomCategory.values()) {
            if (chatRoomCategory.name().equalsIgnoreCase(value)) {
                return chatRoomCategory;
            }
        }
        throw new CommonException(ENUM_ROOM_CATEGORY_INVALID);
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
