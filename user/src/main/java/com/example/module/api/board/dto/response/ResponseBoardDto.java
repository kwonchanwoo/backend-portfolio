package com.example.module.api.board.dto.response;

import com.example.module.entity.Board;
import com.example.module.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseBoardDto {
    private Long id; //id (PK)
    private String title; // 제목
    private String contents; // 내용
    private int views; // 조회수
    private String author; // 작성자
    private int board_comment_count; // 게시판 댓글 수

    public ResponseBoardDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.contents  = board.getContents();
        this.views  = board.getViews();
        this.author = board.getCreatedMember().getUserId();
        this.board_comment_count = board.getBoardComments()
                .stream()
                .filter(boardComment -> boardComment.getBoardComment()==null) // 맨처음 댓글만 필터해서 가져옴. 맨처음 댓글은 부모값이 없음
                .mapToInt(this::getBoardComment).sum();
    }

    public int getBoardComment(BoardComment boardComment){
        if(boardComment.getBoardComments() == null || boardComment.getBoardComments().isEmpty()){ // 자식 댓글이없을땐 자기자신만 리턴
            return 1;
        }else{
            return 1+ boardComment.getBoardComments().stream().mapToInt(this::getBoardComment).sum(); // 자식 댓글이있을 때, 재귀함수 호출
        }
    }
}
