# 섹션 1

## Welcome page
src > main > resources > static > index.html

## Controller
웹 어플리케이션 진입점
- 클래스 위에 @Controller를 명시해줘야 한다.
```java
@Controller
public class HelloController {
    @GetMapping("hello")
    public String hello(Model model){
        model.addAttribute("data", "hello!!!");
        return "hello";
    }
}
```
- data는 Model에서 key값이다.
- hello!!!는 Model에서 value값이다.
  - 즉, key는 data이고, value는 hello!!!이다.
- @GetMapping
  - 웹 어플리케이션에서 /hello가 들어온다면, 아래의 메소드`public String hello(Model model){}`를 호출한다.

## hello.html(Template Engine)
src > main > resources > templates > hello.html
```html
<html xmlns:th="http://www.thymeleaf.org">
```
- 템플릿 엔진으로 thymeleaf를 사용하는 것을 명시한다.
  - 타임리프 문법을 사용하기 위해선 th를 사용해야 한다.
```html
<p th:text="'안녕하세요. ' + ${data}" >안녕하세요. 손님</p>
```
- Controller에서 /hello가 들어오면(hello.html에 접근하면), `model.addAttribute("data", "hello!");`에 의해서 hello.html의 `data(${data})`부분이 `hello!!!`로 치환된다.
  - 동작이 다 끝나면 `return "hello"`부분이 실행된다.
    - return은 templates의 hello.html을 반환한다.
      - 즉, 모델에 data: hello!!!를 넘기면서 hello.html을 반환한다.
  - 즉, 리턴 값으로 문자를 반환하면 viewResolver가 화면을 찾아서 처리한다.
    - viewResolver는 resources > templates 폴더 아래에서 리턴 값을 찾는다.

# 섹션2

## MVC
- Model, View, Controller

## RequestParam
- 파라미터를 받을 때 RequestParam을 사용한다.
  - 전달받은 파라미터를 model로 넘겨준다.
```java
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("name") String name, Model model){
        model.addAttribute("name", name);
        return "hello-template";
        }
```
- RequestParam으로 전달받은 name을 model에 넘겨준다.
  - model에는 key가 name이고 value는 parameter가 받은 값이 들어간다. 
  

## API
```java
    @GetMapping("hello-string")
    @ResponseBody
    public String helloString(@RequestParam("name") String name){
        return "hello " + name;
    }
```
- ResponseBody
  - http에서 body에 데이터를 직접 넣어준다.
    - `localhost:8080/hello-string?name=kamja`에 접근 시
      - http의 body에는 hello kamja가 들어간다.

```java
@GetMapping("hello-api")
    @ResponseBody
    public Hello helloApi(@RequestParam("name") String name){
        Hello hello = new Hello();
        hello.setName(name);
        return hello;
    }

    static class Hello{
        private String name;

        public String getName(){
            return name;
        }
        public void setName(String name){
            this.name = name;
        }
    }
```
- 객체를 만들어서 객체를 반환하면 JSON 형식으로 반환된다.

@ResponseBody 사용 시
- HTTP의 BODY에 문자 내용을 직접 반환한다.
- viewResolver 대신 HttpMessageConverter가 동작한다.
- 문자는 StringHttpMessageConverter가 동작한다.
- 객체는 MappingJackson2HttpMessageConverter가 동작한다.
- 기타 처리는 HttpMessageConverter가 동작한다.

# 섹션 3
domain

repository
- 저장소
  - 회원 객체를 저장한다.
  - MemberRepositoryInterface
  - repository를 만들면 구현체를 만들어야 한다.
    - MemoryMemberRepository

MemberRepository.java
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;


import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    // 회원 저장
    Member save(Member member);
    // 회원 검색
    Optional<Member> findById(Long id);
    Optional<Member> findByName(String name);
    // 모든 회원 반환
    List<Member> findAll();
}
```

MemoryMemberRepository.java
- 실제로 구현
```java
package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.*;

public class MemoryMemberRepository implements MemberRepository{
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;

    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Member> findByName(String name) {
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
        // findAny는 하나라도 찾는다라는 의미이다.
        // 즉, member에서 하나라도 찾으면 return 한다.
        // 이 결과가 Optional로 반환된다.
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }
}


```

Optional
- java8에서 null을 처리하는 방법
  - null을 감싸서 반환한다.

@AfterEach
- 메서드가 실행이 끝날때 마다 동작한다.
```java
@AfterEach
    public void afterEach(){
        repository.clearStore();
    }
```
- 하나의 테스트가 끝날 때마다 저장소를 비워주기 위해 사용했다.

store.clear
- 저장소를 비운다.
```java
    public void clearStore(){
        store.clear();
    }
```

null이 있을 수 있으면 optional로 감싸서 반환한다.
```java
        Optional<Member> result = memberRepository.findByName(member.getName());
        result.ifPresent(m -> {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        });
```

# 섹션 4
스프링 빈 등록하는 2가지 방법
1. 컴포넌트 스캔과 자동 의존관계 설정
- @Component 어노테이션이 있으면 스프링 빈으로 자동 등록된다.
  - @Service, @Repository, @Controller 안에 @Component가 정의되어있음
- @Autowired
2. 자바 코드로 직접 스프링 빈 등록
- SpringConfig 파일 생성
  - @Bean으로 직접 등록
```java
package hello.hellospring;

import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import hello.hellospring.service.MemberService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringConfig {
    @Bean
    public MemberService memberService(){
        return new MemberService(memberRepository());
    }
    @Bean
    public MemberRepository memberRepository() {
        return new MemoryMemberRepository();
    }


}
```