package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.domain.User;
import org.example.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * 스프링 시큐리티에서 사용자의 정보를 가져오기 위해선 UserDetailsService 인터페이스를 구현해야 한다.
 * UserDetailsService 인터페이스를 구현하면 loadUserByUsername() 메서드를 오버라이딩해서 사용자 정보를 가져오는 로직을 작성해야 한다.
 * */
@RequiredArgsConstructor
@Service
// 스프링 시큐리티에서 사용자 정보를 가져오는 인터페이스
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    // 사용자 이름(email)으로 사용자의 정보를 가져오는 메서드
    @Override
    public User loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(email));
    }
}
