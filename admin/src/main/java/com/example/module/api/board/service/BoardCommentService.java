package com.example.module.api.board.service;

import com.example.module.api.board.dto.request.RequestBoardComment;
import com.example.module.api.board.dto.response.BoardCommentDto;
import com.example.module.entity.Board;
import com.example.module.entity.BoardComment;
import com.example.module.repository.board.BoardCommentRepository;
import com.example.module.util.CommonException;
import com.example.module.util._Enum.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardCommentService {
    private final BoardCommentRepository boardCommentRepository;

    public List<BoardCommentDto> getBoardCommentList(Board board) {
        return boardCommentRepository
                .findByBoardAndBoardCommentNullOrderBySortAsc(board)// 메인 댓글 조회(메인 댓글은 부모값이 없음 parent_id가 null인것만 체크)
                .stream()
                .map(BoardCommentDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void postBoardComment(Board board, RequestBoardComment requestBoardComment) {
        BoardComment boardComment = null;
        // 댓글 부모값이 있는지 체크
        if (requestBoardComment.getParentId() != null) {
            boardComment = boardCommentRepository
                    .findById(requestBoardComment.getParentId())
                    .orElseThrow(() -> new CommonException(ErrorCode.BOARD_COMMENT_NOT_FOUND));

            // 형제중 정렬값이 가장높은 값 조회
            int max_sort = Objects.requireNonNull(boardCommentRepository
                    .findFirstByBoardAndBoard_DeletedFalseAndBoardCommentAndDeletedFalseOrderBySortDesc(board, boardComment)
                    .orElse(new BoardComment())).getSort(); // 멤버변수 int에 기본값으로 0이들어가니 상관없음
            boardCommentRepository.save(BoardComment.builder()
                    .board(board)
                    .boardComment(boardComment)
                    .sort(max_sort + 1)
                    .contents(requestBoardComment.getContents())
                    .build());
        } else {
            boardCommentRepository.save(BoardComment.builder()
                    .board(board)
                    .contents(requestBoardComment.getContents())
                    .build());
        }
    }
}
