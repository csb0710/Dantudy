package com.study.board2.entity.Study;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class CodingStudy {
    private List<Integer> languages;

    private List<Integer> period;

    private List<Integer> times;

    private List<Integer> time;

    private List<Integer> people;

    public void checkNull() {
        if(languages.size() == 0) {
            for (int i = 1; i < 5; i++)
                languages.add(i);
        }
        if(period.size() == 0) {
            for (int i = 1; i < 5; i++)
                period.add(i);
        }
        if(times.size() == 0) {
            for (int i = 1; i < 5; i++)
                times.add(i);
        }
        if(time.size() == 0) {
            for (int i = 1; i < 5; i++)
                time.add(i);
        }
        if(period.size() == 0) {
            for (int i = 1; i < 5; i++)
                period.add(i);
        }
        if(people.size() == 0) {
            for (int i = 1; i < 5; i++)
                people.add(i);
        }
    }
}
