package com.study.board2.controller;

import com.study.board2.dto.CommentDTO;
import com.study.board2.service.BoardService;
import com.study.board2.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {
    @Autowired
    private final CommentService commentService;

    @Autowired
    private final BoardService boardService;

    @PostMapping("/save")
    public ResponseEntity save(@ModelAttribute CommentDTO commentDTO) {
        Long saveResult = commentService.save(commentDTO);
        if(saveResult != null) {
            // 성공 후 댓글 목록 리턴
            List<CommentDTO> commentDTOList = commentService.findAll(commentDTO.getBoardId());
            //boardService.reduceHits(commentDTO.getBoardId());

            return new ResponseEntity(commentDTOList, HttpStatus.OK);
        }
        else {
            return new ResponseEntity("댓글이 존재하지 않습니다", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/delete/{id}")
    public String checkDelete(@PathVariable("id") Long id, Model model, HttpServletRequest request, Authentication authentication) {
        String username = authentication.getName();
        Integer boardId = commentService.removeComment(id, username);
        boardService.reduceHits(boardId);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
