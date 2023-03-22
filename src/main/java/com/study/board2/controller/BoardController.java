package com.study.board2.controller;

import com.study.board2.dto.CommentDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.User;
import com.study.board2.repository.CommentRepository;
import com.study.board2.service.BoardService;
import com.study.board2.service.CommentService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/board/list")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            String searchKeyword) {

        Page<Board> list = null;

        if(searchKeyword != null) {
            System.out.println(searchKeyword);
            list = boardService.boardSearchList(searchKeyword, pagebale);
        }
        else {
            list = boardService.boardList(pagebale);
        }



        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(list.getTotalPages(), nowPage + 5);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @PostMapping("/account/login")
    public String logi2n() {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return "account/login";
    }

    @GetMapping("/board/view")
    public String boardView(Model model, Integer id) {
        boardService.checkHits(id);
        model.addAttribute("board", boardService.boardView(id));

        return "boardview";
    }

    @PostMapping("/board/remove")
    public String boardRemove(Integer id, Board board) {
        String tempPassword = boardService.boardView(id).getPassword();

        if(!tempPassword.equals(board.getPassword())) {
            return "wrongpassword";
        }

        boardService.removeView(id);

        return "redirect:/board/list";
    }

    @GetMapping("/board/checkmodi/{id}")
    public String checkModify(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "checkmodify";
    }

    @GetMapping("/board/checkdel/{id}")
    public String checkDelete(@PathVariable("id") Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));
        System.out.println(id);

        return "checkdelete";
    }

//    @PostMapping("/board/modify/{id}")
//    public String boardModify(@PathVariable("id") Integer id, Model model, Board board) {
//        String tempPassword = boardService.boardView(id).getPassword();
//
//        if(!tempPassword.equals(board.getPassword())) {
//            return "wrongpassword";
//        }
//
//        model.addAttribute("board", boardService.boardView(id));
//
//        return "boardmodify";
//    }

    @PostMapping("/board/update/{id}")
    public String boardUpdate(@PathVariable("id") Integer id, Model model, Board board, MultipartFile file) throws IOException {
        Board boardTemp = boardService.boardView(id);

        boardTemp.setTitle(board.getTitle());
        boardTemp.setContent(board.getContent());

        boardService.write(boardTemp, file);

        model.addAttribute("message", "글 작성이 완료되었습니다");
        model.addAttribute("searchUrl", "/board/list");

        System.out.println("작성되었습니다");

        return "message";
    }

    @GetMapping("/board/form")
    public String form(Model model, @RequestParam(required = false) Integer id, Authentication authentication) {
        if(id == null) {
            Board exboard = new Board();
            model.addAttribute("board", exboard);
            return "form";
        }
        else {
            String username = authentication.getName();
            List<CommentDTO> commentDTOList = commentService.findAll(id);
            boardService.checkHits(id);
            model.addAttribute("commentUsername", username);
            model.addAttribute("commentList", commentDTOList);
            model.addAttribute("board", boardService.boardView(id));

            return "form2";
        }
    }

    @PostMapping("/board/form")
    public String postForm(@Valid Board board, @NotNull BindingResult bindingResult, Authentication authentication) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        String username = authentication.getName();
        boardService.write2(board, username);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify")
    public String modifyForm(@RequestParam(required = false) Integer id, Model model) {
        model.addAttribute("board", boardService.boardView(id));

        return "form3";
    }

    @PostMapping("/board/modify")
    public String modifyForm2(@Valid Board board, @NotNull BindingResult bindingResult, Authentication authentication, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        if (request.getHeader("Referer") != null) {
            String username = authentication.getName();

            boardService.write2(board, username);
            boardService.reduceHits(board.getId());
            return "redirect:/board/form?id=" + board.getId();
        } else {

            return "redirect:/main";
        }
    }

}
