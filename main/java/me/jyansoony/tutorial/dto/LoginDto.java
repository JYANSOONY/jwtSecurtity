package me.jyansoony.tutorial.dto;

import lombok.*;

import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//로그인 관련 dto
public class LoginDto {
//@Valid 관련 어노테이션들
    @NonNull
    @Size(min=3, max=50)
    private String username;

    @NonNull
    @Size(min=3, max=100)
    private String password;
}
