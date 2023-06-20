package org.example;

import lombok.RequiredArgsConstructor;
import org.example.service.UserDetailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {
    private final UserDetailService userService;


    /**
     * 스프링 시큐리티 기능 비활성화
     * 즉, 스프링 시큐리티의 모든 기능을 사용하지 않도록 설정한다.
     * 즉, 인증, 인가 서비스를 모두 적용하지 않는다.
     * 일반적으로 정적 리소스(이미지, HTML) 파일에 설정한다.
     * */
    @Bean
    public WebSecurityCustomizer configure(){
        return (web) -> web.ignoring()
                .requestMatchers(toH2Console())
                .requestMatchers("/static/**");
    }


    /**
     * 특정 HTTP 요청에 대한 웹 기반 보안 구성
     * */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        return http
                /**
                 * 인증, 인가 설정
                 * requestMatchers() => 특정 요청과 일치하는 url에 대한 엑세스를 설정한다.
                 * permitAll() => 누구나 접근이 가능하게 설정한다.
                 * anyRequest() => 위에서 설정한 url 이외의 요청에 대해서 설정한다.
                 * authenticated() => 별도의 인가는 필요하지 않지만 인증이 접근할 수 있다.
                 * */
                .authorizeRequests() // 인증, 인가 설정
                .requestMatchers("/login", "/signup", "/user").permitAll()
                .anyRequest().authenticated()
                .and()
                /**
                 * 폼 기반 로그인 설정
                 * loginPage() => 로그인 페이지 경로 설정
                 * defaultSuccessUrl() => 로그인이 완료되었을 때 이동할 경로 설정
                 * */
                .formLogin() // 폼 기반 로그인 설정
                .loginPage("/login")
                .defaultSuccessUrl("/articles")
                /**
                 * 로그아웃 설정
                 * logoutSuccessUrl() => 로그아웃이 완료되었을 때 이동할 경로 설정
                 * invalidateHttpSession() => 로그아웃 이후 세션을 전체 삭제할지 여부 설정
                 * */
                .and()
                .logout() // 로그아웃 설정
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .and()
                .csrf().disable() // csrf 비활성화, CSRF 공격을 방지하기 위해 활성화해야 한다.
                .build();
    }
    /**
     *  인증 관리자 관련 설정
     *  사용자 정보를 가져올 서비스를 재정의하거나, 인증 방법(LDAP, JDBC) 기반 인증 등을 설정할 때 사용한다.
     * */
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService)
        throws Exception{
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                /**
                 * 사용자 정보 서비스 설정
                 * userDetailsService() => 사용자 정보를 가져올 서비스를 서비스를 설정한다.
                 * 이때 설정하는 서비스 클래스는 반드시 UserDetailService를 상속받은 클래스여야 한다.
                 * passwordEncoder() => 비밀번호를 암호화하기 위한 인코더를 설정한다.
                 * */
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }
    /**
     * 패스워드 인코더로 사용할 빈 등록
     * */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
