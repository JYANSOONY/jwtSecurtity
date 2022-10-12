package me.jyansoony.tutorial.controller;

import me.jyansoony.tutorial.dto.LoginDto;
import me.jyansoony.tutorial.dto.TokenDto;
import me.jyansoony.tutorial.jwt.JwtFilter;
import me.jyansoony.tutorial.jwt.TokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
//로그인 api를 추가하기 위해
public class AuthController {
    //TokenProvider 를 주입받고
    private final TokenProvider tokenProvider;
    //authenticationManagerBuilder 를 주입받는다
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }
//로그인 api경로는 /api/authenticate 이고 post로 요청받는다.
    @PostMapping("/authenticate")
    //LoginDto 의 username, password를 파라미터로 받고
    public ResponseEntity<TokenDto> authorize(@Valid @RequestBody LoginDto loginDto) {
        //username, password를 이용해 UsernamePasswordAuthenticationToken 을 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        //authenticationToken를 이용해 authentication 객체를 생성하려고
        //authenticate 메소드가 실행이 될 때 loadUserByUsername 메소드가 실행된다. (CustomUserDetailsService.java)
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        //그 결과 값을 가지고 SecurityContextHolder 저장을 하고
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //authentication 객체를 createToken 메소드를 통해 JWT Token을 생성한다.
        String jwt = tokenProvider.createToken(authentication);
        //jwt 토큰을 response 헤더에도 넣어주고
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        //tokenDto 를 이용해서 response 바디에도 넣어 리턴하게 된다.
        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }
}