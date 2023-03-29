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
    private Map<Integer, String> languages;

    private Map<Integer, String> period;

    private Map<Integer, String> times;

    private Map<Integer, String> time;

    private Map<Integer, String> people;


    public StudyService() {
        languages = new HashMap<>();
        languages.put(1, "자바");
        languages.put(2, "파이썬");
        languages.put(3, "JS");
        languages.put(4, "c언어");

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

//    public static Board getBoard(Board board, BoardDTO boardDTO) {
//        StudyService studyService = new StudyService();
//
//        board.setLanguages(studyService.getLanguages().get(boardDTO.getLanguages()));
//        board.setPeriod(studyService.getPeriod().get(boardDTO.getPeriod()));
//        board.setTimes(studyService.getTimes().get(boardDTO.getTimes()));
//        board.setTime(studyService.getTime().get(boardDTO.getTime()));
//        board.setPeople(studyService.getPeople().get(boardDTO.getPeople()));
//
//        return board;
//    }
}