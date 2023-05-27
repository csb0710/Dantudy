package com.study.board2.service;

import com.study.board2.dto.BoardDTO;
import com.study.board2.entity.Board;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Service
public class StudyService {

    private Map<Integer, String> period;

    private Map<Integer, String> times;

    private Map<Integer, String> time;

    private Map<Integer, String> people;


    public StudyService() {
        period = new HashMap<>();
        period.put(1, "4주");
        period.put(2, "12주");
        period.put(3, "24주");
        period.put(4, "48주");

        times = new HashMap<>();
        for(int i = 1; i < 8; i++) {
            times.put(i, i+"번");
        }

        time = new HashMap<>();
        time.put(1, "오전");
        time.put(2, "점심 직후");
        time.put(3, "저녁");

        people = new HashMap<>();
        for(int i = 1; i < 5; i++) {
            people.put(i, i+"명");
        }

    }
}
