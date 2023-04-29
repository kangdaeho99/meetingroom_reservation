package com.room.reservation.repository;


import com.room.reservation.entity.Member;
import com.room.reservation.entity.Review;
import com.room.reservation.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    /**
     * Description
     * Review 클래스의 Member에 대한 Fetch 방식이 Lazy이기에 Review객체와 MEmber 객체를 한번에 조회 불가
     * Transactional 적용해도 각 Review 객체의 getMember(), getEmail()을 처리할때마다 MEmber객체 로딩필요
     * 1) Query를 이용해 조인처리
     * 2) EntityGraph를 이용해 Review객체를 가져올때 Member 객체를 로딩
     * EntityGraph를 통해 연관관계의 FETCH 속성값을 Eager로 로딩하도록 설정
     */
    @EntityGraph(attributePaths = {"member"}, type=EntityGraph.EntityGraphType.FETCH)
    List<Review> findByRoom(Room room);


    @Modifying
    @Query("delete from Review rv where rv.member = :member")
    void deleteByMember(Member member);

    @Modifying
    @Query("delete from Review rv where rv.room.rno =:rno")
    void deleteByRno(Long rno);
}
