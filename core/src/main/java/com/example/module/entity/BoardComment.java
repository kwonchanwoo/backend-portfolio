package com.example.module.entity;

import com.example.module.util.BaseEntity;
import jakarta.persistence.*;
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
@Table(uniqueConstraints={
        @UniqueConstraint(
                name= "BoardCommentUnique",
                columnNames={"board_comment_id", "sort"} // 복합 unique 설정
        )
})
public class BoardComment extends BaseEntity {


    @ManyToOne
    @JoinColumn(name = "board_comment_id")
    private BoardComment boardComment;

    @OneToMany(mappedBy = "boardComment", orphanRemoval = true,cascade = {CascadeType.PERSIST, CascadeType.REMOVE},fetch = FetchType.LAZY)
    private List<BoardComment> boardComments;

    @Column
    private Long targetCommentId; // 대댓글 대상 id

    private int sort;

    @Column(columnDefinition = "varchar(255) DEFAULT NULL")
    private String contents;

    @ManyToOne()
    @JoinColumn(name = "board_id")
    private Board board;

}
