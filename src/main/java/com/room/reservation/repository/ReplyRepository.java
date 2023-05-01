package com.room.reservation.repository;

import com.room.reservation.entity.Reply;
import com.room.reservation.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("delete from Reply rp where rp.room.rno = :rno")
    void deleteByRno(Long rno);
    
    //게시물로 댓글 가져오기
    List<Reply> getRepliesByRoomOrderByReplyno(Room room);
}
