package com.example.module.entity;

import com.example.module.util.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;



@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class File extends BaseEntity {
    // 저장된 서버 파일명
    @Column(unique = true, nullable = false)
    private String storedName;
    // 사용자 업로드 파일명
    @Column(nullable = false)
    private String originName;
    // 파일 경로
    @Column(nullable = false)
    private String path;
    // 파일 설명
    @Column
    private String description;
    // 파일 크기
    @Column(nullable = false)
    private long size;
    // 다운로드 횟수
    @Column(nullable = false)
    @ColumnDefault("0")
    private int downloadCnt;

    @Column(columnDefinition = "varchar(30) not null comment 'mime 타입'")
    private String mime;

    @Column(columnDefinition = "varchar(10) NOT NULL COMMENT '파일 확장자'")
    private String extension;

    @ManyToOne
    @JoinColumn(name = "file_category_id")
    private FileCategory fileCategory;

}
