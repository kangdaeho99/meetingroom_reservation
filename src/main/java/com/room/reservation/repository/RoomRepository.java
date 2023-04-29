package com.room.reservation.repository;

import com.room.reservation.entity.Room;
import com.room.reservation.repository.room.RoomQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQueryDslRepository, QuerydslPredicateExecutor<Room> {
    /**
     * Mysql 특징으로 RoomImage를 사용할시 에러. QueryDSL으로 대체
     */
    Page<Object[]> getListPage(Pageable pageable);

    @Query("SELECT r, ri, avg(coalesce(rv.grade,0)), count(distinct rv)" +
            " from Room r " +
            " left outer join RoomImage ri on ri.room = r" +
            " left outer join Review rv on rv.room = r" +
            " where r.rno = :rno" +
            " group by ri")
    List<Object[]> getRoomWithAll(Long rno);


}
