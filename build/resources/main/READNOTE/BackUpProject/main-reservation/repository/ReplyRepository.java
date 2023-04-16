package com.room.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


/**
 * Description :
 *
 *
 *
 */
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    /**
     * Description : 게시물 삭제 시 미리 댓글을 모두 삭제하기 위한 함수입니다.
     * JPQL을 이용해서 update, delete를 실행하기 위해서는 @Modifying 어노테이션을 같이 추가해야합니다.
     */
    @Modifying
    @Query("delete from Reply r where r.board.bno =:bno")
    void deleteByBno(Long bno);

    /**
     * Descrpiton :
     * 게시물로 댓글 목록 가져오기
     */
    List<Reply> getRepliesByBoardOrderByRno(Board board);
}
