package me.jyansoony.tutorial.dto;

//토큰 정보를 reponse할 때 사용할 dto
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenDto {

    private String token;
}
