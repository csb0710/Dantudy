package com.study.board2.controller;

import com.study.board2.entity.User;
import com.study.board2.service.AccountService;
import com.study.board2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @GetMapping("/login")
    public String login() {
        return "account/login";
    }

    @PostMapping("/register/exists")
    //@ResponseBody
    public ResponseEntity checkUsername(@RequestParam("username") String username) {
        boolean result = userService.checkExist(username);
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @GetMapping("/register")
    public String register() {

        return "account/register";
    }

    @PostMapping("/register")
    public String register(@Valid User user, Errors errors, Model model) {
        if(errors.hasErrors()) {
            model.addAttribute("user", user);

            Map<String, String> validatorResult = accountService.validateHandling(errors);
            for(String key : validatorResult.keySet()) {
                System.out.println(key);
                model.addAttribute(key+"if", true);
                model.addAttribute(key, validatorResult.get(key));
            }

            return "account/register";
        }
        user.setCounting(0);
        user.setRating("평가 등급");
        user.setScore(0.0);
        user.setScore2(0);

        userService.save(user);

        return "redirect:/account/login";
    }
}
