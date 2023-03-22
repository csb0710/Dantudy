package com.study.board2.controller;

import com.study.board2.entity.User;
import com.study.board2.service.AccountService;
import com.study.board2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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

        userService.save(user);

        return "redirect:/board/list";
    }
}
