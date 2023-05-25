package com.study.board2.dto;

import com.study.board2.entity.Study.CodingStudyForm;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class FormDTO {
    BoardDTO boardDTO;

    CodingStudyForm codingStudy;

    public FormDTO() {
        boardDTO = new BoardDTO();
        codingStudy = new CodingStudyForm();
    }

}
