package me.jyansoony.tutorial.config;

import me.jyansoony.tutorial.jwt.JwtAccessDeniedHandler;
import me.jyansoony.tutorial.jwt.JwtAuthenticationEntryPoint;
import me.jyansoony.tutorial.jwt.JwtSecurityConfig;
import me.jyansoony.tutorial.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@EnableWebSecurity //기본적인 웹보안
@EnableGlobalMethodSecurity(prePostEnabled = true) // preAuthorized를 메소드별로 적용하려고 추가
public class SecurityConfig {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    //jwt 클래스들을 주입 받음
    public SecurityConfig(
            TokenProvider tokenProvider,
            JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint,
            JwtAccessDeniedHandler jwtAccessDeniedHandler
    ) {
        this.tokenProvider = tokenProvider;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.jwtAccessDeniedHandler = jwtAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().antMatchers("/h2-console/**"
                , "/favicon.ico"
                , "/error");
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()

                //exception을 Handling할 때 authenticationEntryPoint와 accessDeniedHandler를 만들었던 클래스들로 추가해줌
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // enable h2-console 위한 설정
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                // 세션을 사용하지 않기 때문에 STATELESS로 설정한다.
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                //로그인api ,회원가입 api는 토큰이 없느 ㄴ상태에서 요청이 들어오기 때문에 모두 permitAll 해줌
                .and()
                .authorizeRequests() //http 리퀘스트로 받는 것들 접근제한
                .antMatchers("/api/hello").permitAll() //인증없이 접근허용
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/signup").permitAll()
                .anyRequest().authenticated()//나머지 요청들은 인증을 받아야함

                //JwtFilter를 통해 addFilterBefore로 등록했던 JwtSecurityConfig클래스 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

                return http.build();
    }
}
