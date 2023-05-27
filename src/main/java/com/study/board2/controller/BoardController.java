package com.study.board2.controller;

import com.study.board2.dto.BoardDTO;
import com.study.board2.dto.CommentDTO;
import com.study.board2.entity.Board;
import com.study.board2.entity.Study.StudyForm;
import com.study.board2.entity.User;
import com.study.board2.repository.BoardRepository;
import com.study.board2.repository.CommentRepository;
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
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;

@Controller
public class BoardController {
    @Autowired
    private BoardService boardService;
    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private StudyService studyService;

    @GetMapping("/board/list")
    public String boardList(@RequestParam(required = false) Integer type, StudyForm studyInform, Model model,
                            @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pagebale,
                            String searchKeyword) {

        Page<Board> list = null;

        if(studyInform != null && (studyInform.getPeople() != null || studyInform.getTime() != null || studyInform.getTimes() != null || studyInform.getPeriod() != null) || searchKeyword != null) {
            System.out.println(boardRepository.findAll(BoardSpecification.containTitle(searchKeyword)).size() + "////////////////////////////");
            if(type == null) {
                type = studyInform.getType();
            }
            list = boardService.coditionFind(studyInform, type, searchKeyword, pagebale);
        }
        else {
            list = boardService.boardList(type, pagebale);
        }



        int nowPage = list.getPageable().getPageNumber() + 1;
        int startPage = Math.max(1, nowPage - 4);
        int endPage = Math.min(list.getTotalPages(), nowPage + 5);

        if(endPage==0) {
            endPage=1;
        }

        StudyForm studyModel = new StudyForm();

        model.addAttribute("studyModel", studyModel);
        model.addAttribute("period", studyService.getPeriod());
        model.addAttribute("times", studyService.getTimes());
        model.addAttribute("time", studyService.getTime());
        model.addAttribute("people", studyService.getPeople());

        model.addAttribute("list", list);
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("type", type);

        return "board/boardlist";
    }

    @GetMapping("/board/studyInform")
    public String boardList(@RequestParam("studyId") Integer studyId, Model model, Principal principal) {
        List<User> list = boardRepository.findById(studyId).get().getMember();
        Board board = boardRepository.findById(studyId).get();
        String currentUsername = principal.getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("board", board);
        model.addAttribute("master", board.getUser());
        model.addAttribute("period", studyService.getPeriod().get(board.getPeriod()));
        model.addAttribute("times", studyService.getTimes().get(board.getTimes()));
        model.addAttribute("time", studyService.getTime().get(board.getTime()));
        model.addAttribute("people", studyService.getPeople().get(board.getPeople()));
        model.addAttribute("list", list);

        System.out.println(currentUser.getId() + " /////////////////////////////////");
        System.out.println(board.getUser().getId()+ " @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
        return "board/studyInform";
    }



    @PostMapping("/account/login")
    public String logi2n() {
        System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");
        return "account/login";
    }

    @GetMapping("/board/form")
    public String form(Model model, @RequestParam(required = false) Integer id, @RequestParam(required = false) Integer type, Authentication authentication, Principal principal) {
        if(id == null) {
            BoardDTO exboard = new BoardDTO();
            exboard.setType(type);

            model.addAttribute("board", exboard);
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
            for(CommentDTO c : commentDTOList) {
                System.out.println(c.getUserRating()+"!@#####################");
            }
            boardService.checkHits(id);
            BoardDTO newBoardDTO = BoardDTO.toBoardDTO(boardService.boardView(id));

            model.addAttribute("commentUsername", username);
            model.addAttribute("commentList", commentDTOList);
            model.addAttribute("board", newBoardDTO);

            model.addAttribute("period", studyService.getPeriod().get(newBoardDTO.getPeriod()));
            model.addAttribute("times", studyService.getTimes().get(newBoardDTO.getTimes()));
            model.addAttribute("time", studyService.getTime().get(newBoardDTO.getTime()));
            model.addAttribute("people", studyService.getPeople().get(newBoardDTO.getPeople()));
            String currentUsername = principal.getName();
            User currentUser = userRepository.findByUsername(currentUsername);

            model.addAttribute("currenUserRating", currentUser.getRating());
            model.addAttribute("currentUserId", currentUser.getId());
            System.out.println(newBoardDTO.getId() +" "+ currentUser.getId() + "@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            return "board/form2";
        }
    }

//    @GetMapping("/board/form2")
//    public String form2(Model model, @RequestParam(required = false) Integer id, Authentication authentication) {
//        if(id == null) {
//            FormDTO formDTO = new FormDTO();
//
//            model.addAttribute("formDTO", formDTO);
//            model.addAttribute("period", studyService.getPeriod());
//            model.addAttribute("times", studyService.getTimes());
//            model.addAttribute("time", studyService.getTime());
//            model.addAttribute("people", studyService.getPeople());
//
//            return "board/formtest";
//        }
//        else {
//
//            String username = authentication.getName();
//            Long userId = userRepository.findByUsername(username).getId();
//            List<CommentDTO> commentDTOList = commentService.findAll(id, userId);
//            boardService.checkHits(id);
//            BoardDTO newBoardDTO = BoardDTO.toBoardDTO(boardService.boardView(id));
//            model.addAttribute("commentUsername", username);
//            model.addAttribute("commentList", commentDTOList);
//            model.addAttribute("board", newBoardDTO);
//
//            return "board/formtest";
//        }
//    }

    @PostMapping("/board/form")
    public String postForm(@Valid BoardDTO boardDTO, @NotNull BindingResult bindingResult, Authentication authentication) throws IOException {
        if (bindingResult.hasErrors()) {
            return "board/form";
        }

        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        user.setCounting(user.getCounting()+1);
        boardDTO.checkNull();
        boardDTO.setCompleted(0);
        boardService.write2(boardDTO, username);
        return "redirect:/board/list?type=" + boardDTO.getType();
    }

//    @PostMapping("/board/form2")
//    public String postForm2(@Valid FormDTO formDTO, @NotNull BindingResult bindingResult, Authentication authentication) throws IOException {
//        if (bindingResult.hasErrors()) {
//            return "board/form2";
//        }
//
//        String username = authentication.getName();
//        boardService.write2(formDTO.getBoardDTO(), username);
//        return "redirect:/board/list?type=" + formDTO.getBoardDTO().getType();
//    }

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
    @PostMapping("/board/updateUrl")
    public String updateUrl(@RequestParam("boardId") Integer boardId, @RequestParam("url") String url) {
        Board board = boardRepository.findById(boardId).get();
        board.setKakaoURL(url);
        boardRepository.save(board);

        return "redirect:/board/list";
    }
    @PostMapping("/board/acceptStudy")
    public String acceptComment(@RequestParam("boardId") Integer boardId, @RequestParam("commentId") Integer commentId) {
        Board board = boardRepository.findById(boardId).get();
        User user = commentRepository.findById(Long.valueOf(commentId)).get().getUser();

        if (board == null || user == null) {
            return "error";
        }

        if (!board.getMember().contains(user)) {
            board.getMember().add(user);
            user.getCStudies().add(board);
            user.setCounting(user.getCounting()+1);
            userRepository.save(user);
            boardService.write(board);
        }

        return "redirect:/board/list"; // 원래 페이지로의 리다이렉션을 수행합니다.
    }

    @PostMapping("/board/saveScore")
    public String saveScore(@RequestParam("rating") int rating, @RequestParam("memberId") int memberId, HttpServletRequest request) {
        User user = userRepository.findById(Long.valueOf(memberId)).get();
        Integer people = user.getScore2()+1;
        Double tempScore = user.getScore()+rating;

        user.setScore2(people);
        user.setScore(tempScore/people);

        userRepository.save(user);

        return "redirect:"+ request.getHeader("Referer");
    }

    @GetMapping("/board/completeDelete")
    public String completeDel(@RequestParam("studyId") Integer studyId, @RequestParam Integer userId, Model model) {
        User user = userRepository.findById(Long.valueOf(userId)).get();
        Board board = boardRepository.findById(studyId).get();

        user.getCStudies().remove(board);

        userRepository.save(user);

        return "index";
    }
    @GetMapping("/board/completeInform")
    public String completeList(@RequestParam("studyId") Integer studyId, Model model, Principal principal) {
        List<User> list = boardRepository.findById(studyId).get().getMember();
        Board board = boardRepository.findById(studyId).get();
        String currentUsername = principal.getName();
        User currentUser = userRepository.findByUsername(currentUsername);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("board", board);
        model.addAttribute("master", board.getUser());
        model.addAttribute("period", studyService.getPeriod().get(board.getPeriod()));
        model.addAttribute("times", studyService.getTimes().get(board.getTimes()));
        model.addAttribute("time", studyService.getTime().get(board.getTime()));
        model.addAttribute("people", studyService.getPeople().get(board.getPeople()));
        model.addAttribute("list", list);


        return "board/studyEndForm";
    }

    @GetMapping("/board/delete")
    public String boardDelete(@RequestParam Integer id, HttpServletRequest request) {
        if (request.getHeader("Referer") != null) {
            Board board = boardRepository.findById(id).get();
            List<User> users = board.getMember();
            User master = board.getUser();

            for(User user : users) {
                user.setCounting(user.getCounting()-1);
                user.getCStudies().remove(board);
                userRepository.save(user);
            }

            master.setCounting(master.getCounting()-1);
            userRepository.save(master);


            System.out.println(master.getId());

            board.setMember(null);
            boardRepository.save(board);
            boardService.deleteView(id);

            return "redirect:/board/list?type=" + board.getType();
        } else {

            return "redirect:/main";
        }
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "admin/accessDenied";
    }


    @GetMapping("/users/inform")
    public String userInform(@RequestParam(required = false) Integer id, @RequestParam(required = false) Integer commentId, @RequestParam(required = false) String username,  Model model, Principal principal) {
        Long id2;
        if(id == null && commentId != null) {
             id2 = commentRepository.findById(Long.valueOf(commentId)).get().getUser().getId();
        }
        else if(username != null) {
            id2 = userRepository.findByUsername(username).getId();
        }
        else {
            id2 = Long.valueOf(id);
        }
        String currentUsername = principal.getName();
        User currentUser = userRepository.findByUsername(currentUsername);
        User user = userRepository.findById(id2).get();
        user.checkRating();
        userRepository.save(user);

        model.addAttribute("currentUserId", currentUser.getId());
        model.addAttribute("list", user.getBoards());
        model.addAttribute("list2", user.getCStudies());
        model.addAttribute("user", user);

        return "user/userPage";
    }
}
