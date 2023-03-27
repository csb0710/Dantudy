package com.study.board2.controller;

import com.study.board2.dto.BoardDTO;
import com.study.board2.dto.CommentDTO;
import com.study.board2.dto.FormDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.Study.CodingStudy;
import com.study.board2.repository.BoardRepository;
import com.study.board2.repository.UserRepository;
import com.study.board2.service.BoardService;
import com.study.board2.service.CommentService;
import com.study.board2.service.StudyService;
import com.study.board2.specification.BoardSpecification;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StudyService studyService;

    @GetMapping("/board/list")
    public String boardList(CodingStudy codingStudy, Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            String searchKeyword) {

        Page<Board> list = null;

        if(codingStudy != null && codingStudy.getLanguages() != null) {
            System.out.println(boardRepository.findAll(BoardSpecification.containTitle(searchKeyword)).size() + "////////////////////////////");
            list = boardService.coditionFind(codingStudy, searchKeyword, pagebale);
        }
        else {
            list = boardService.boardList(pagebale);
        }



        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(list.getTotalPages(), nowPage + 5);

        CodingStudy codingStudy2 = new CodingStudy();

        model.addAttribute("CodingStudy", codingStudy2);
        model.addAttribute("languages", studyService.getLanguages());
        model.addAttribute("period", studyService.getPeriod());
        model.addAttribute("times", studyService.getTimes());
        model.addAttribute("time", studyService.getTime());
        model.addAttribute("people", studyService.getPeople());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "board/boardlist";
    }

    @PostMapping("/account/login")
    public String logi2n() {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return "account/login";
    }

    @GetMapping("/board/form")
    public String form(Model model, @RequestParam(required = false) Integer id, Authentication authentication) {
        if(id == null) {
            CodingStudy codingStudy2 = new CodingStudy();
            BoardDTO exboard = new BoardDTO();

            model.addAttribute("board", exboard);
            model.addAttribute("languages", studyService.getLanguages());
            model.addAttribute("period", studyService.getPeriod());
            model.addAttribute("times", studyService.getTimes());
            model.addAttribute("time", studyService.getTime());
            model.addAttribute("people", studyService.getPeople());



            return "board/form";
        }
        else {
            String username = authentication.getName();
            Long userId = userRepository.findByUsername(username).getId();
            List<CommentDTO> commentDTOList = commentService.findAll(id, userId);
            boardService.checkHits(id);
            BoardDTO newBoardDTO = BoardDTO.toBoardDTO(boardService.boardView(id));

            model.addAttribute("commentUsername", username);
            model.addAttribute("commentList", commentDTOList);
            model.addAttribute("board", newBoardDTO);

            model.addAttribute("languages", studyService.getLanguages().get(newBoardDTO.getLanguages()));
            model.addAttribute("period", studyService.getPeriod().get(newBoardDTO.getPeriod()));
            model.addAttribute("times", studyService.getTimes().get(newBoardDTO.getTimes()));
            model.addAttribute("time", studyService.getTime().get(newBoardDTO.getTime()));
            model.addAttribute("people", studyService.getPeople().get(newBoardDTO.getPeople()));

            return "board/form2";
        }
    }

    @GetMapping("/board/form2")
    public String form2(Model model, @RequestParam(required = false) Integer id, Authentication authentication) {
        if(id == null) {
            CodingStudy codingStudy2 = new CodingStudy();
            BoardDTO exboard = new BoardDTO();
            FormDTO formDTO = new FormDTO();

            model.addAttribute("formDTO", formDTO);
            model.addAttribute("languages", studyService.getLanguages());
            model.addAttribute("period", studyService.getPeriod());
            model.addAttribute("times", studyService.getTimes());
            model.addAttribute("time", studyService.getTime());
            model.addAttribute("people", studyService.getPeople());

            return "board/formtest";
        }
        else {
            String username = authentication.getName();
            Long userId = userRepository.findByUsername(username).getId();
            List<CommentDTO> commentDTOList = commentService.findAll(id, userId);
            boardService.checkHits(id);
            BoardDTO newBoardDTO = BoardDTO.toBoardDTO(boardService.boardView(id));
            model.addAttribute("commentUsername", username);
            model.addAttribute("commentList", commentDTOList);
            model.addAttribute("board", newBoardDTO);

            return "board/formtest";
        }
    }

    @PostMapping("/board/form")
    public String postForm(@Valid BoardDTO boardDTO, @NotNull BindingResult bindingResult, Authentication authentication) throws IOException {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }

        String username = authentication.getName();
        boardService.write2(boardDTO, username);
        return "redirect:/board/list";
    }

    @PostMapping("/board/form2")
    public String postForm2(@Valid FormDTO formDTO, @NotNull BindingResult bindingResult, Authentication authentication) throws IOException {
        if (bindingResult.hasErrors()) {
            return "board/form2";
        }

        String username = authentication.getName();
        boardService.write2(formDTO.getBoardDTO(), username);
        return "redirect:/board/list";
    }

    @GetMapping("/board/modify")
    public String modifyForm(@RequestParam(required = false) Integer id, Model model) {
        BoardDTO newBoardDTO = BoardDTO.toBoardDTO(boardService.boardView(id));
        model.addAttribute("board", newBoardDTO);

        return "board/form3";
    }

    @PostMapping("/board/modify")
    public String modifyForm2(@Valid BoardDTO boardDTO, @NotNull BindingResult bindingResult, Authentication authentication, HttpServletRequest request) throws IOException {
        if (bindingResult.hasErrors()) {
            return "form";
        }

        if (request.getHeader("Referer") != null) {
            String username = authentication.getName();

            boardService.update(boardDTO, username);
            return "redirect:/board/form?id=" + boardDTO.getId();
        } else {

            return "redirect:/main";
        }
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam Integer id, HttpServletRequest request) {
        if (request.getHeader("Referer") != null) {
            String username = boardService.deleteView(id);

            return "redirect:/admin/user/board?searchKeyword=" + username;
        } else {

            return "redirect:/main";
        }
    }

    @GetMapping("/board/test")
    public String testmethod(Model model) {
        CodingStudy codingStudy = new CodingStudy();

        model.addAttribute("CodingStudy", codingStudy);
        model.addAttribute("languages", studyService.getLanguages());
        model.addAttribute("period", studyService.getPeriod());
        model.addAttribute("times", studyService.getTimes());
        model.addAttribute("time", studyService.getTime());
        model.addAttribute("people", studyService.getPeople());

        return "board/test";
    }


    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "admin/accessDenied";
    }

}
