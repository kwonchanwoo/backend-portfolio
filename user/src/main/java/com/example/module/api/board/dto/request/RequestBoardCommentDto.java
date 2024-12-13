package com.example.module.api.board.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RequestBoardCommentDto {
    private Long boardId; // 게시판 id
    private Long boardCommentId; // 메인 댓글 id
    private Long targetCommentId; // 대상 댓글 id
    private String title;
    private String contents;
}
