package com.study.board2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.study.board2.dto.BoardDTO;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity // 클래스가 db에 있는 테이블을 의미한다는 것을 나타냄
@Data
@Getter
@Setter
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @NotNull
    @Size(min=2, max=30)
    private String title;

    private String content;

    private Integer hits = 0;

    private Integer period;

    private Integer times;

    private Integer time;

    private Integer people;

    private String kakaoURL;

    private Integer type;

    private Integer completed;

    @ManyToMany(mappedBy = "cStudies", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private List<User> member = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    private LocalDateTime createdAt;

    public static Board toSaveEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(0);
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());
        boardEntity.setType(boardDTO.getType());
        boardEntity.setCreatedAt(boardDTO.getCreateAt());
        boardEntity.setCompleted(boardDTO.getCompleted());

        return boardEntity;
    }

    public static Board toUpdateEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(boardDTO.getHits());
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());
        boardEntity.setType(boardDTO.getType());
        boardEntity.setCreatedAt(boardDTO.getCreateAt());
        boardEntity.setCompleted(boardDTO.getCompleted());

        return boardEntity;
    }

    public static Board toSaveFileEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(0);
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());
        boardEntity.setType(boardDTO.getType());
        boardEntity.setCreatedAt(boardDTO.getCreateAt());
        boardEntity.setCompleted(boardDTO.getCompleted());

        return boardEntity;
    }


}
