package me.jyansoony.tutorial.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity //DB테이블과 1:1 매핑되는 객체
@Table(name = "users") //테이블 명을 users로 사용하기 위해 해당 어노테이션 사용
@Getter//생성자 어노테이션 (롬복)
@Setter//생성자 어노테이션 (롬복)
@Builder//생성자 어노테이션 (롬복)
@AllArgsConstructor //생성자 어노테이션 (롬복)
@NoArgsConstructor //생성자 어노테이션 (롬복)
public class User {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) //자동증가되는 프라이머리 키
    private Long userId;

    @Column(name = "username", length = 50, unique = true)
    private String username;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany //User객체와 권한 객체의 다대다 관계를 일대다 다대일 관계의 조인테이블로 정의 했다는 뜻
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;
}