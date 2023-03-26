package com.study.board2.dto;

import com.study.board2.entity.Comment;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommentDTO {
    private Long id;
    private String commentWriter;
    private String commentContents;
    private Integer boardId;
    private LocalDateTime commentCreatedTime;
    private Long userId;

    public static CommentDTO toCommentDTO(Comment comment, Integer boardId, Long userId) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setCommentWriter(comment.getCommentWriter());
        commentDTO.setCommentContents(comment.getCommentContents());
        commentDTO.setCommentCreatedTime(comment.getCreatedTime());
        //사용하고 싶으면 service 메서드에 @Transactional 필수
        //commentDTO.setBoardId(new Long(comment.getBoard().getId()));
        commentDTO.setBoardId(boardId);
        commentDTO.setUserId(userId);

        return commentDTO;

    }
}
