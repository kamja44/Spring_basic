package org.example.controller;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.util.List;

@Controller
public class ExampleController {
    /**
     *  모델 객체는 따로 생성할 필요 없이 코드처럼 인자로 선언하기만 하면 스프링이 알아서 만들어준다.
     *  addAttribute() 메서드로 모델에 값을 저장한다.
     *  person 키에 examplePerson 정보를 저장한다.
     *  today 키에 LocalDate.now() 정보를 저장한다.
     *  resources>templates 디렉터리에서 example.html을 찾은 다음 웹 브라우저에서 해당 파일을 반환한다.
     * */
    @GetMapping("/thymeleaf/example")
    public String thymeleafExample(Model model){ // view로 데이터를 넘겨주는 모델 객체

        Person examplePerson = new Person();
        examplePerson.setId(1L);
        examplePerson.setName("홍길동");
        examplePerson.setAge(11);
        examplePerson.setHobbies(List.of("운동", "독서"));

        model.addAttribute("person", examplePerson); // person 객체에 저장
        model.addAttribute("today", LocalDate.now());

        return "example"; // example.html라는 뷰 조회
    }

    @Setter
    @Getter
    class Person{
        private Long id;
        private String name;
        private int age;
        private List<String> hobbies;
    }
}

