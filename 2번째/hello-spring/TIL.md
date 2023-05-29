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