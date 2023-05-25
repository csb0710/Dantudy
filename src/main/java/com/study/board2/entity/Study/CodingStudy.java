package com.study.board2.entity.Study;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.study.board2.entity.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@Getter
@Setter
public class CodingStudy {
    private Integer period;

    private Integer time;

    private Integer times;

    private Integer people;

    private String deadline;

    private Integer type;

    @ManyToMany(mappedBy = "cStudies")
    @JsonIgnore
    private List<User> member = new ArrayList<>();

}
