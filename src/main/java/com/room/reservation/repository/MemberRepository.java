package com.room.reservation.repository;

import com.room.reservation.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    /**
     * 이메일을 기준으로 조회하고, 일반 로그인 사용자와 소셜 로그인 사용자를 구분하기 위해 로직을 처리합니다.
     * E
     * @param email : email
     * @param social : 소셜로그인 여부 확인
     * @return
     */
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select m from Member m where m.fromSocial = :social and m.email = :email")
    Optional<Member> findByEmail(String email, boolean social);


}
