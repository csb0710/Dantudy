package com.study.board2.specification;

import com.study.board2.entity.Board;
import com.study.board2.entity.Study.CodingStudy;
import org.springframework.data.jpa.domain.Specification;


public class BoardSpecification {
    public static Specification<Board> equalLaguage(CodingStudy codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("languages").in(codingStudy.getLanguages()));
    }

    public static Specification<Board> equalPeriod(CodingStudy codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("period").in(codingStudy.getPeriod()));
    }

    public static Specification<Board> equalTimes(CodingStudy codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("times").in(codingStudy.getTimes()));
    }

    public static Specification<Board> equalTime(CodingStudy codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("time").in(codingStudy.getTime()));
    }

    public static Specification<Board> equalPeople(CodingStudy codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("people").in(codingStudy.getPeople()));
    }

    public static Specification<Board> containTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%"+title+"%");
    }

}
