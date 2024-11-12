package com.example.module.api.file.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseFileDto {
    private Long id;
    private String fileName;
    private String fileCategory;
    private String description;
    private String memberName;
    private String userId;
    private long size;
    private String createdAt;
    private String updatedAt;



    public ResponseFileDto(Long id,String fileName, String fileCategory, String description, String memberName,
                           String userId,long size,LocalDateTime createdAt,LocalDateTime updatedAt){
        this.id = id;
        this.fileName = fileName;
        this.fileCategory = fileCategory;
        this.description = description;
        this.memberName = memberName;
        this.userId = userId;
        this.size = size;
        this.createdAt = createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.updatedAt = updatedAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
