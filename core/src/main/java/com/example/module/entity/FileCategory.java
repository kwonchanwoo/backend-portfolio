package com.example.module.entity;

import com.example.module.util.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder // 부모 클래스의 값을 builder 할수있음
@AllArgsConstructor
@NoArgsConstructor
public class FileCategory extends BaseEntity {
    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "fileCategory", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
    private List<File> files;

}
