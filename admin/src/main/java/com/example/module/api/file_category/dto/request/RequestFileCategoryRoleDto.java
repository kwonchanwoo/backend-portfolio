package com.example.module.api.file_category.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestFileCategoryRoleDto {
    private List<Long> assignMemberIds; // 권한부여 id 리스트
    private List<Long> revokeMemberIds; // 권한철회 id 리스트
    private Long file_category_id;
}
