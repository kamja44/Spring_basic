package org.example.config.jwt;

import io.jsonwebtoken.Jwts;
import org.example.domain.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * generateToken() 검증 테스트
     * 토큰을 생성하는 메서드를 테스트하는 메서드
     * given => 토큰에 유저 정보를 추가하기 위한 테스트 유저를 만든다.
     * when => 토큰 제공자의 generateToken() 메서드를 호출하여 토큰을 만든다.
     * then => jjwt 라이브러리를 사용하여 토큰을 복호화한다. 토큰을 만들 때 클레임으로 넣어둔 id값이 given절에서 만든 유저 ID와 동일한지 확인한다.
     * */
    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달하여 토큰을 만들 수 있다.")
    @Test
    void generateToken() {
        // given
        User testUser = userRepository.save(User.builder()
                .email("user@gmail.com")
                .password("test")
                .build());

        // when
        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testUser.getId());
    }

    /**
     * validToken() 검증 테스트
     * 토큰이 유효한 토큰인지 검증하는 메서드
     *
     * validToken_invalidToken()
     * given => jjwt 라이브러리를 이용하여 토큰을 생성한다.
     * when => 토큰 제공자의 validToken() 메서드를 호출하여 유효한 토큰인지 검증한 뒤 결과값을 반환받는다.
     * then => 반환값이 false인 것을 확인한다.
     *
     * validToken_validToken()
     * given => jjwt 라이브러리를 이용하여 토큰을 생성한다.
     * when => 토큰 제공자의 validToken() 메서드를 호출하여 유효한 토큰인지 검증한 뒤 결과값을 반환받는다.
     * then => 반환값이 true인 것을 확인한다.
     * */

    @DisplayName("validToken(): 만료된 토큰인 때에 유효성 검증에 실패한다.")
    @Test
    void validToken_invalidToken(){
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isFalse();
    }

    @DisplayName("validToken(): 유효한 토큰일 때에 유효성 검증에 성공한다.")
    @Test
    void validToken_validToken(){
        // given
        String token = JwtFactory.withDefaultValues().createToken(jwtProperties);
        // when
        boolean result = tokenProvider.validToken(token);
        // then
        assertThat(result).isTrue();
    }

    /**
     * getAuthentication() 검증 테스트
     * 토큰을 전달받아 인증 정보를 담은 객체 Authentication을 반환하는 메서드인 getAuthentication()을 테스트한다.
     * given => jjwt 라이브러리를 사용하여 토큰을 생성한다.
     * 토큰을 생성할 때 토큰의 이름인 subject는 user@gmail.com라는 값을 사용한다.
     * when => 토큰 제공자의 getAithentication() 메서드를 호출하여 인증 객체를 반환받는다.
     * then => 반환받은 인증 객체의 유저 이름을 가져와 given 절에서 설정한 subject값인 user@gmail.com과 같은지 확인한다.
     * */

    @DisplayName("getAuthentication(): 토큰 기반으로 인증 정보를 가져올 수 있다.")
    @Test
    void getAuthentication(){
        // given
        String userEmail = "user@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);
        // when
        Authentication authentication = tokenProvider.getAuthentication(token);
        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }


    /**
     * getUserId() 검증 테스트
     * getUserId() 메서드는 토큰 기반으로 유저의 ID를 가져오는 메서드를 테스트하는 메서드이다.
     * given => jjwt 라이브러리를 사용하여 토큰을 생성한다.
     * 키는 id이고, 값은 1로 클레임을추가한다.
     * when => 토큰 제공자의 getUserId() 메서드를 호출하여 유저 ID를 반환받는다.
     * then => 반환받은 유저 ID가 given 절에서 설정한 유저 ID값인 1과 같은지 확인한다.
     * */
    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    void getUserId(){
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);
        // when
        Long userIdByToken = tokenProvider.getUserId(token);
        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}
