package org.example.springbootdeveloper;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본 생성자
// 엔티티는 반드시 기본 생성자가 있어야 하고, 접근 제어자는 public 또는 protected여야 한다.
// public 보다 protected가 더 안전하므로 접근 제어자가 protectedㅇ니 기본 생성자를 생성한다.
@AllArgsConstructor
@Getter
@Entity // 엔티티로 지정
public class Member {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1씩 증가
    @Column(name = "id", nullable = false)
    private Long id; // DB 테이블의 id 컬럼과 매칭된다.

    @Column(name="name", nullable = false) // name이라는 not null 컬럼과 매핑
    private String name; // DB 테이블의 name 컬럼과 매칭된다.

}
