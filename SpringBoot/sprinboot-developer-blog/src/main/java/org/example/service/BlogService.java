package org.example.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.domain.Article;
import org.example.dto.AddArticleRequest;
import org.example.dto.UpdateArticleRequest;
import org.example.repository.BlogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor // final이 붙거나 @NotNull이 붙은 필드의 생성자 추가
@Service // 빈 등록
public class BlogService {
    private final BlogRepository blogRepository;

    // 블로그 글 추가 메서드
    public Article save(AddArticleRequest request, String userName){
        return blogRepository.save(request.toEntity(userName));
    }

    // DB에 저장되어 있는 글을 모두 가져오는 findAll() 메서드 추가
    public List<Article> findAll(){
        return blogRepository.findAll();
    }
    // 블로그 글 하나를 조회하는 메서드
    public Article findById(long id){
        return blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: "+id));
    }
    // 블로그 글 삭제
    public void delete(long id){
        Article article = blogRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("not found : "+id));
        authorizeArticleAuthor(article);
        blogRepository.delete(article);

    }
    /**
     * @Transactional 애너테이션은 매칭한 메서드를 하나의 트랜잭션으로 묶는 역할을 한다.
     * 스프링에서 트랜잭션을 적용하기 위해 다른 작업 없이 @Transactional 애너테이션만 사용하면 된다.
     * 트랜잭션은 DB의 데이터를 바꾸기 위해 묶은 작업의 단위이다.
     * */
    @Transactional // 트랜잭션 메서드
    public Article update(long id, UpdateArticleRequest request){
        Article article = blogRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("not found: "+id));

        authorizeArticleAuthor(article);
        article.update(request.getTitle(), request.getContent());

        return article;
    }


    // 게시글을 작성한 유저인지 확인
    private static void authorizeArticleAuthor(Article article){
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if(!article.getAuthor().equals(userName)){
            throw new IllegalArgumentException("not authorized");
        }
    }
}
