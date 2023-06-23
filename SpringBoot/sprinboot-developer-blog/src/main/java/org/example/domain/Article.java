package org.example.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Article {
    @Id // id 필드를 기본키로 지정
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 기본키 자동으로 1씩 증가
    @Column(name="id", updatable = false)
    private Long id;

    @Column(name = "title", nullable = false) // 'title'이라는 not null 컬럼과 매핑
    private String title;

    @Column(name="content", nullable = false)
    private String content;

    @CreatedDate // 엔티티가 생성될 떄 생성 시간 저장
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate // 엔티티가 수정될 때 수정 ㅣ간 저장
    @Column(name="updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "author", nullable = false)
    private String author;


    @Builder // 빌더패턴으로 객체생성
    public Article(String author, String title, String content){
        this.author = author;
        this.title = title;
        this.content = content;
    }
    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }


    /**
     * protected Article(){}은 NoArgsConstructor(access = AccessLevel.PROTECTED)로 대체한다.
     * GETTER 함수들은 class 위에 @Getter 애네테이션으로 대체한다.
    protected Article(){} // 기본 생성자

    // Getter
    public Long getId(){
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getContent(){
        return content;
    }
    */
    /**
     * @CreatedAt 애너테이션을 사용하면 엔티티가 생성될 때 생성 시간을 created_at 컬럼에 저장한다.
     * */

}
