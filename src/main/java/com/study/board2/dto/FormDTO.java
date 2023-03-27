package com.study.board2.dto;

import com.study.board2.entity.Study.CodingStudy;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FormDTO {
    BoardDTO boardDTO;

    CodingStudy codingStudy;

    public FormDTO() {
        boardDTO = new BoardDTO();
        codingStudy = new CodingStudy();
    }

}
