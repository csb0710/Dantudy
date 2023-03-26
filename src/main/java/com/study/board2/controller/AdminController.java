package com.study.board2.controller;

import com.study.board2.entity.Board;
import com.study.board2.entity.Comment;
import com.study.board2.entity.User;
import com.study.board2.repository.CommentRepository;
import com.study.board2.repository.UserRepository;
import com.study.board2.service.BoardService;
import com.study.board2.service.CommentService;
import com.study.board2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;


    @GetMapping("/main")
    public String adminMain() {
        return "admin/main";
    }

    @GetMapping("/user/list")
    public String userList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            String searchKeyword) {

        Page<User> list = null;

        if(searchKeyword != null) {
            list = userService.userSearchList(searchKeyword, pagebale);
        }
        else {
            list = userService.userList(pagebale);
        }



        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(list.getTotalPages(), nowPage + 5);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "admin/userlist";
    }

    @GetMapping("/user/delete/{id}")
    public String checkDelete(@PathVariable("id") Long id, HttpServletRequest request) {
        userService.removeUser(id);
        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }

    @GetMapping("/user/board")
    public String boardList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            @RequestParam String searchKeyword) {



        Page<Board> list = null;

        User findUser = userRepository.findByUsername(searchKeyword);
        List<Board> users = findUser.getBoards();

        int start = (int)pagebale.getOffset();
        int end = Math.min((start+pagebale.getPageSize()), users.size());
        list = new PageImpl<>(users.subList(start, end), pagebale, users.size());

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.max(Math.min(list.getTotalPages(), nowPage + 5), 1);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("user", findUser);

        return "admin/userboardlist";
    }

    @GetMapping("/user/comment")
    public String commentList(Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            @RequestParam String searchKeyword) {



        Page<Comment> list = null;

        User findUser = userRepository.findByUsername(searchKeyword);
        List<Comment> comments = commentRepository.findByCommentWriterContaining(findUser.getUsername(), pagebale);

        int start = (int)pagebale.getOffset();
        int end = Math.min((start+pagebale.getPageSize()), comments.size());
        list = new PageImpl<>(comments.subList(start, end), pagebale, comments.size());

        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(list.getTotalPages(), nowPage + 5);

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("user", findUser);

        return "admin/usercommentlist";
    }

    @GetMapping("/user/comment/delete")
    public String commentDelete(@RequestParam Long id, Model model, HttpServletRequest request, Authentication authentication) {
        String username = authentication.getName();
        Integer boardId = commentService.removeCommentOnlyId(id);

        String referer = request.getHeader("Referer");
        return "redirect:"+ referer;
    }
}
