package com.example.module.api.board.dto.response;

import com.example.module.entity.BoardComment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {
    private Long id; // pk
    private int sort; // 정렬순서
    private String contents;
    private List<BoardCommentDto> commentDtoList;
    public BoardCommentDto(BoardComment boardComment) {
        this.id = boardComment.getId();
        this.sort = boardComment.getSort();
        this.contents = boardComment.getContents();
        this.commentDtoList = boardComment
                .getBoardComments()
                .stream()
                .map(BoardCommentDto::new)
                .collect(Collectors.toList());
    }
}
