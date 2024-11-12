package com.example.module.util._Enum;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
    // token
    TOKEN_INVALID(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, 403, "토큰의 유효기간 만료"),

    TOKEN_ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "잘못된 접근입니다."), // 권한이 없다고 표현하면 악용 될수있어서 잘못된 접근으로 처리

    TOKEN_UNSUPPORTED(HttpStatus.BAD_REQUEST, 400, "지원되지 않는 JWT 토큰 형식입니다."),

    //common
    ACCESS_DENIED(HttpStatus.FORBIDDEN, 403, "잘못된 접근입니다."), // // 권한이 없다고 표현하면 악용 될수있어서 잘못된 접근으로 처리

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Member not found"),
    MEMBER_DUPLICATED(HttpStatus.CONFLICT, 409, "Member is duplicated"),

    // board
    //    BOARD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Board_Comment not found"),
    //    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "Board not found"),

    // redis
    REDIS_COMMAND_EXECUTION(HttpStatus.SERVICE_UNAVAILABLE,503,"redis에서 문제가 발생 하였습니다."),

    // login
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, 401, "로그인에 실패하였습니다."),

    // enum
    ENUM_GENDER_INVALID(HttpStatus.BAD_REQUEST, 400, "성별을 잘못 입력 하였 습니다."),
    ENUM_BOARD_CATEGORY_INVALID(HttpStatus.BAD_REQUEST, 400, "게시판 종류를 잘못 입력 하였 습니다."),

    // fileCategory
    FILE_CATEGORY_DUPLICATED(HttpStatus.CONFLICT,409,"FileCategory is duplicated"),
    FILE_CATEGORY_NOT_EXISTS(HttpStatus.BAD_REQUEST,400,"FileCategory is not exists"),

    // fileCategoryRole
    FILE_CATEGORY_ROLE_NOT_EXISTS(HttpStatus.FORBIDDEN,403 ,"해당 파일 메뉴에 대한 권한이 없습니다."),

    /**
     * 파일 관련
     */
    FILE_NOT_FOUND(HttpStatus.PRECONDITION_FAILED, 412,"파일이 전달되지 않았습니다."),
    FILE_EMPTY(HttpStatus.BAD_REQUEST,400,"파일을 선택해주세요."),
    FILE_NOT_EMPTY(HttpStatus.BAD_REQUEST,400,"하위 파일들이 존재합니다 해당 파일을 삭제해주세요."),
    FILE_TYPE_INVALID(HttpStatus.BAD_REQUEST,400, "파일 타입이 맞지 않습니다."),
    FILE_NOT_NULL(HttpStatus.BAD_REQUEST, 400,"파일 ID 값이 잘못되었습니다."),
    FILE_SIZE_EXCEEDED(HttpStatus.BAD_REQUEST, 400,"허용되는 파일 크기를 벗어났습니다. (동영상 최대 1GB, 이미지는 50MB)"),
    FILE_TEMPLATE_IMAGE_SIZE_EXCEED(HttpStatus.BAD_REQUEST,400, "템플릿 이미지 크기는 최대 1080X1920입니다."),
    FILE_EXTENSION_INVALID(HttpStatus.BAD_REQUEST,400, "파일 형식이 맞지 않습니다."),
    FILE_UPLOAD_FAIL(HttpStatus.BAD_REQUEST,400,"파일 업로드에 실패하였습니다."),
    EMPTY_FILE_NAME(HttpStatus.BAD_REQUEST,400,"파일 이름을 확인해주세요."),

    /**
     *  Sort 관련
     */
    SORT_KEY_NOT_EXISTS(HttpStatus.BAD_REQUEST,400 ,"정렬 키값이 해당 엔터티에 존재하지않습니다." ),

    /**
     *  게시판 관련
     */
    BOARD_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,404,"해당 게시판 댓글을 찾을수없습니다.");

    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, Integer code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
