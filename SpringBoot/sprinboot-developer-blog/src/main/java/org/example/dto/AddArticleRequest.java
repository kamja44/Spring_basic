package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.example.domain.Article;

@NoArgsConstructor // 기본 생성자 추가
@AllArgsConstructor // 모든 필드 값을 파라미터로 받는 생성자 추가
@Getter
public class AddArticleRequest {

    private String title;
    private String content;


    /**
     * toEntity() => 빌더 패턴을 사용하여 DTO를 엔티티로 만들어주는 메서드
     * 추후 블로그 글을 추가할 때 저장할 엔티티로 변환하는 용도로 사용
     * */
    public Article toEntity(String author){ // 생성자를 사용해 객체 생성
        return Article.builder()
                .title(title)
                .content(content)
                .author(author)
                .build();
    }
}
