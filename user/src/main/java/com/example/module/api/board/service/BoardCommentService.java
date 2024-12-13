package com.example.module.api.board.service;

import com.example.module.api.board.dto.request.RequestBoardCommentDto;
import com.example.module.api.board.dto.request.RequestBoardIdDto;
import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.entity.Board;
import com.example.module.entity.BoardComment;
import com.example.module.repository.board.BoardCommentRepository;
import com.example.module.repository.board.BoardRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;
    private final BoardRepository boardRepository;

    public List<BoardCommentDto> getBoardCommentList(RequestBoardIdDto requestBoardIdDto) {
        Board board = boardRepository.findById(requestBoardIdDto.getBoardId()).orElseThrow(() -> new CommonException(ErrorCode.BOARD_NOT_FOUND));

        return boardCommentRepository
                .getBoardCommentList(board);
    }

    @Transactional
    public void postBoardComment(RequestBoardCommentDto requestBoardCommentDto) {

        //게시판 체크
        Board board = boardRepository
                .findByIdAndDeletedFalse(requestBoardCommentDto.getBoardId())
                .orElseThrow(() -> new CommonException(ErrorCode.BOARD_NOT_FOUND));

        // 서브 댓글
        if (requestBoardCommentDto.getBoardCommentId() != null) {
            BoardComment boardComment = boardCommentRepository
                    .findById(requestBoardCommentDto.getBoardCommentId())
                    .orElseThrow(() -> new CommonException(ErrorCode.BOARD_COMMENT_NOT_FOUND));

            // 대상 댓글 체크
            boardCommentRepository
                    .findById(requestBoardCommentDto.getTargetCommentId())
                    .orElseThrow(() -> new CommonException(ErrorCode.BOARD_COMMENT_NOT_FOUND));

            // 형제중 정렬값이 가장높은 값 조회
            int max_sort = Objects.requireNonNull(boardCommentRepository
                    .findFirstByBoardAndBoard_DeletedFalseAndBoardCommentAndDeletedFalseOrderBySortDesc(board, boardComment)
                    .orElse(new BoardComment())).getSort(); // 멤버변수 int에 기본값으로 0이들어가니 상관없음
            boardCommentRepository.save(BoardComment.builder()
                    .board(board)
                    .boardComment(boardComment)
                    .targetCommentId(requestBoardCommentDto.getTargetCommentId())
                    .sort(max_sort + 1)
                    .contents(requestBoardCommentDto.getContents())
                    .build());
        } else {  // 메인 댓글
            boardCommentRepository.save(BoardComment.builder()
                    .board(board)
                    .contents(requestBoardCommentDto.getContents())
                    .build());
        }
    }
}
