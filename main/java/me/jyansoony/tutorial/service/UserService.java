package me.jyansoony.tutorial.service;

import java.util.Collections;
import java.util.Optional;

import me.jyansoony.tutorial.dto.UserDto;
import me.jyansoony.tutorial.entity.Authority;
import me.jyansoony.tutorial.entity.User;
//import me.jyansoony.tutorial.exception.DuplicateMemberException;
//import me.jyansoony.tutorial.exception.NotFoundMemberException;
import me.jyansoony.tutorial.repository.UserRepository;
import me.jyansoony.tutorial.util.SecurityUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    //호원가입 로직을 수행하는 메소드
    public User signup(UserDto userDto) {
        //userDto안에 username을 기준으로 이미 db에 해당 정보가 있는지 찾아보고
        if (userRepository.findOneWithAuthoritiesByUsername(userDto.getUsername()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        //없다면 권한 정보를 만들고
        Authority authority = Authority.builder()
                //회원가입할떄 권한은 ROLE_USER만 부여 됨
                .authorityName("ROLE_USER")
                .build();
        //user정보를 생성해
        User user = User.builder()
                .username(userDto.getUsername())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .nickname(userDto.getNickname())
                .authorities(Collections.singleton(authority))
                .activated(true)
                .build();
        //userRepository의 save메소드를 통해 유저정보와 권한 정보를 db에 정보를 저장함.
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    //username을 기준으로 유저객체와 권한 정보를 가져오고
    public Optional<User> getUserWithAuthorities(String username) {
        return userRepository.findOneWithAuthoritiesByUsername(username);
    }

    @Transactional(readOnly = true)
    //현재 SecurityContext에 저장된 username의 정보만 가져옴
    public Optional<User> getMyUserWithAuthorities() {
        return SecurityUtil.getCurrentUsername()
                        .flatMap(userRepository::findOneWithAuthoritiesByUsername);
    }
}
