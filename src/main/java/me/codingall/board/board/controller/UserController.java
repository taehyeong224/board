package me.codingall.board.board.controller;

import me.codingall.board.board.dto.UserDto;
import me.codingall.board.board.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "/index";
    }

    // 회원가입 페이지
    @GetMapping("/user/signup")
    public String dispSignup() {
        return "/board/signup";
    }
    // 회원가입 처리
    @PostMapping("/user/signup")
    public String execSignup(UserDto userDto) {
        userService.joinUser(userDto);

        return "redirect:/user/login";
    }

    // 로그인 페이지
    @GetMapping("/user/login")
    public String dispLogin() {
        return "/board/login";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String dispLoginResult() {
        return "/board/loginSuccess";
    }

    // 로그아웃 결과 페이지
    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "/board/logout";
    }

    // 접근 거부 페이지
    @GetMapping("/user/denied")
    public String dispDenied() {
        return "/board/denied";
    }

    // 내 정보 페이지
    @GetMapping("/user/info")
    public String dispMyInfo() {
        return "/board/myinfo";
    }

    // 어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/board/admin";
    }
}
