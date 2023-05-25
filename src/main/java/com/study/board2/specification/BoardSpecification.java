package com.study.board2.specification;

import com.study.board2.entity.Board;
import com.study.board2.entity.Study.CodingStudyForm;
import org.springframework.data.jpa.domain.Specification;


public class BoardSpecification {
    public static Specification<Board> equalPeriod(CodingStudyForm codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("period").in(codingStudy.getPeriod()));
    }

    public static Specification<Board> equalTimes(CodingStudyForm codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("times").in(codingStudy.getTimes()));
    }

    public static Specification<Board> equalTime(CodingStudyForm codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("time").in(codingStudy.getTime()));
    }

    public static Specification<Board> equalPeople(CodingStudyForm codingStudy) {
        return (root, query, criteriaBuilder) -> (root.get("people").in(codingStudy.getPeople()));
    }

    public static Specification<Board> containTitle(String title) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("title"), "%"+title+"%");
    }

    public static Specification<Board> equalType(Integer type) {
        return (root, query, criteriaBuilder) -> (root.get("type").in(type));
    }

}
