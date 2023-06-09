package com.study.board2.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "id은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "닉네임은 필수 입력 값입니다.")
    private String nickname;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;

    private String gender;

    private Integer counting;

    @NotBlank(message = "이메일은 필수 입력 값입니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    private boolean enabled;

    private Double score;

    private Integer score2;

    private String rating;

    private String major;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_Studies",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "board_id"))
    private List<Board> Studies;

    @JsonIgnore
    @ManyToMany
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    //@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Override
    public String toString() {
        return "" + this.id;
    }

    public void checkRating() {
        if(this.score >= 4) {
            this.rating = "모범 스터디원";
        }
        else if(this.score >= 3) {
            this.rating = "보통 스터디원";
        }
        else {
            this.rating = "주의 스터디원";
        }

        if(this.score == 0) {
            this.rating = "평가 없음";
        }
    }
}
