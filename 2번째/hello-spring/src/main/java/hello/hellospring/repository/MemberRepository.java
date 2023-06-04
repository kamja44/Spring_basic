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

