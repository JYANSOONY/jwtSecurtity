package me.jyansoony.tutorial.controller;

import me.jyansoony.tutorial.dto.UserDto;
import me.jyansoony.tutorial.entity.User;
import me.jyansoony.tutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
//import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

//    @GetMapping("/hello")
//    public ResponseEntity<String> hello() {
//        return ResponseEntity.ok("hello");
//    }
//
//    @PostMapping("/test-redirect")
//    public void testRedirect(HttpServletResponse response) throws IOException {
//        response.sendRedirect("/api/user");
//    }

    @PostMapping("/signup")
    //userDto 객체를 파라미터로 받아서 userService의 singup메소드를 호출
    public ResponseEntity<User> signup(
            @Valid @RequestBody UserDto userDto
    ) {
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    //PreAuthorize 어노테이션을 통해서 USER_ROLE, ADMIN_ROLE 두가지 권한을 모두 호출할 수 있는 메ㅑ
    public ResponseEntity<User> getMyUserInfo() {
        return ResponseEntity.ok(userService.getMyUserWithAuthorities().get());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    //PreAuthorize 어노테이션을 통해서 ADMIN 권한만 호출할 수 있는 API
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        //username 을 통해서 유저정보와 권한정보를 리턴
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }
}
