package com.room.reservation.repository;

import com.room.reservation.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply, Long> {

    @Modifying
    @Query("delete from Reply rp where rp.room.rno = :rno")
    void deleteByRno(Long rno);
}
