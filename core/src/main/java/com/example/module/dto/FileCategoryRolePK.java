package com.example.module.dto;

import com.example.module.entity.FileCategory;
import com.example.module.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class FileCategoryRolePK implements Serializable { // 복합키 설정
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "file_category_id")
    private FileCategory fileCategory;

}
