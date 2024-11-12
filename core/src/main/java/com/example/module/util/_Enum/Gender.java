package com.example.module.util._Enum;

import com.example.module.util.CommonException;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import static com.example.module.util._Enum.ErrorCode.ENUM_GENDER_INVALID;

public enum Gender {
    FEMALE,
    MALE;

    @JsonCreator
    public static Gender fromValue(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.name().equalsIgnoreCase(value)) {
                return gender;
            }
        }
        throw new CommonException(ENUM_GENDER_INVALID);
    }

    @JsonValue
    public String getValue() {
        return this.name();
    }
}
