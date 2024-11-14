package com.example.module.dto;

public enum Role {
    SUPER_ADMIN("메인 관리자"),ADMIN("일반 관리자"),USER("유저");
    final String value;

    Role(String value){
        this.value = value;
    }
}
