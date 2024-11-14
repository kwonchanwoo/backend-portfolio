package com.example.module.api.board.service;

import com.example.module.api.board.dto.request.RequestBoardDto;
import com.example.module.api.board.dto.response.ResponseBoardDto;
import com.example.module.entity.Board;
import com.example.module.repository.board.repository.board.BoardRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Builder
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public Page<ResponseBoardDto> getBoardList(Map<String, Object> filters, Pageable pageable) {
        return boardRepository.getBoardList(filters, pageable);
    }

    @Transactional
    public void postBoard(RequestBoardDto requestBoardDto) {
        boardRepository.save((Board.builder()
                        .title(requestBoardDto.getTitle())
                        .contents(requestBoardDto.getContents())
                        .build())
        );
    }
    @Transactional
    public void patchBoard(Board board, RequestBoardDto requestBoardDto) {
        board.setTitle(requestBoardDto.getTitle());
        board.setContents(requestBoardDto.getContents());
        boardRepository.save(board);
    }

    public ResponseBoardDto getBoardDetail(Board board) {
         return new ResponseBoardDto(board);
    }
}
