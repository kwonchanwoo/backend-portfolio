package com.example.module.api.board.controller;

import com.example.module.api.board.dto.request.RequestBoardCommentDto;
import com.example.module.api.board.dto.request.RequestBoardIdDto;
import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.api.board.service.BoardCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("board_comments")
@RequiredArgsConstructor
public class BoardCommentController {
    private final BoardCommentService boardCommentService;

    @GetMapping
    public List<BoardCommentDto> getBoardCommentList(@RequestBody RequestBoardIdDto RequestBoardIdDto) {
        return boardCommentService.getBoardCommentList(RequestBoardIdDto);
    }

    @PostMapping
    public void postBoardComment(@RequestBody RequestBoardCommentDto requestBoardCommentDto) {
        boardCommentService.postBoardComment(requestBoardCommentDto);
    }


}
