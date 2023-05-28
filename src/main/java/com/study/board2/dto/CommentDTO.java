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
    private String userRating;

    public static CommentDTO toCommentDTO(Comment comment, Integer boardId, Long userId, String userRating) {
        CommentDTO commentDTO = new CommentDTO();
        commentDTO.setId(comment.getId());
        commentDTO.setCommentWriter(comment.getCommentWriter());
        commentDTO.setCommentContents(comment.getCommentContents());
        commentDTO.setCommentCreatedTime(comment.getCreatedTime());
        commentDTO.setBoardId(boardId);
        commentDTO.setUserId(userId);
        commentDTO.setUserRating(userRating);

        return commentDTO;

    }
}
