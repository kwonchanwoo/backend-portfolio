package com.example.module.util;

import com.example.module.util._Enum.ErrorCode;
import lombok.Getter;

@Getter
public class CommonException extends RuntimeException{
    private final ErrorCode enumErrorCode;
    private final String message;

    public CommonException(ErrorCode enumErrorCode){
        this.enumErrorCode = enumErrorCode;
        this.message = enumErrorCode.getMessage();
    }
}
