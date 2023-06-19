package org.example.repository;

import org.example.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * findByEmail()메서드의 실제 쿼리
     * FROM users Where email = #{email}
     * */
    Optional<User> findByEmail(String email); // email로 사용자 정보를 가져온다.
}
