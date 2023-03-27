package com.study.board2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.study.board2.dto.BoardDTO;
import com.study.board2.entity.Study.CodingStudy;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.File;
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

    private int fileAttached;

    private Integer languages;

    private Integer period;

    private Integer times;

    private Integer time;

    private Integer people;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "board" , cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFiles = new ArrayList<>();

    public static Board toSaveEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(0);
        boardEntity.setFileAttached(0); // 파일 없음.
        boardEntity.setLanguages(boardDTO.getLanguages());
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());

        return boardEntity;
    }

    public static Board toUpdateEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setId(boardDTO.getId());
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(boardDTO.getHits());
        boardEntity.setLanguages(boardDTO.getLanguages());
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());

        return boardEntity;
    }

    public static Board toSaveFileEntity(BoardDTO boardDTO) {
        Board boardEntity = new Board();
        boardEntity.setTitle(boardDTO.getTitle());
        boardEntity.setContent(boardDTO.getContent());
        boardEntity.setHits(0);
        boardEntity.setFileAttached(1); // 파일 있음.
        boardEntity.setLanguages(boardDTO.getLanguages());
        boardEntity.setPeriod(boardDTO.getPeriod());
        boardEntity.setTimes(boardDTO.getTimes());
        boardEntity.setTime(boardDTO.getTime());
        boardEntity.setPeople(boardDTO.getPeople());

        return boardEntity;
    }

}
