package me.jyansoony.tutorial.repository;

import me.jyansoony.tutorial.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

//User 엔티티에 매핑되는 인터페이스
//JpaRepository 를 extends것으로 findAll, save 등의 메소드를 사용할 수 있게 됨
public interface UserRepository extends JpaRepository<User, Long> {
    //해당 쿼리가 수행될 때 lazy조회가 아닌 eager조회로 authorities 정보를 같이 가져옴
    @EntityGraph(attributePaths = "authorities")
    //username을 기준으로 user정보(권한 포함)을 가져옴
    Optional<User> findOneWithAuthoritiesByUsername(String username);
}
