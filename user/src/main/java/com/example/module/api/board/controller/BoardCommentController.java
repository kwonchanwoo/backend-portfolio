package com.example.module.api.board.controller;

import com.example.module.api.board.dto.request.RequestBoardComment;
import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.api.board.service.BoardCommentService;
import com.example.module.entity.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("board_comments")
@RequiredArgsConstructor
public class BoardCommentController {
    private final BoardCommentService boardCommentService;

    @GetMapping("/{board}")
    public List<BoardCommentDto> getBoardCommentList(@PathVariable(name = "board") Board board) {
        return boardCommentService.getBoardCommentList(board);
    }

    @PostMapping("/{board}")
    public void postBoardComment(@PathVariable(name = "board") Board board,@RequestBody RequestBoardComment requestBoardComment) {
        boardCommentService.postBoardComment(board,requestBoardComment);
    }


}
