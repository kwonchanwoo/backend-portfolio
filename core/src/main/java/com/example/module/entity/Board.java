package com.example.module.entity;

import com.example.module.util.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
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
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class Board extends BaseEntity {
    private String title;

    @Lob
    private String contents;

    private int views; // 조회수

    @OneToMany(mappedBy = "board", orphanRemoval = true,cascade={CascadeType.PERSIST,CascadeType.REMOVE})
    private List<BoardComment> boardComments = new ArrayList<>();

}
