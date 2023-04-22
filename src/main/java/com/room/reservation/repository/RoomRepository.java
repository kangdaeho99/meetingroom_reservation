package com.room.reservation.repository;

import com.room.reservation.entity.Room;
import com.room.reservation.repository.room.RoomQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQueryDslRepository {
    /**
     * Mysql 특징으로 RoomImage를 사용할시 에러. QueryDSL으로 대체
     */
//        @Query("select room, roomImage.uuid, avg(coalesce(review.grade, 0)), count(distinct review) from Room room" +
//                " left outer join RoomImage roomImage on roomImage.room = room" +
//                " left outer join Review review on review.room = room group by room, roomImage.imgName")
//        Page<Object[]> getListPage(Pageable pageable);

//        @Query("")
//        Page<Object[]> getListPage(Pageable pageable);

    //        @Query("select room, roomImage.uuid, avg(coalesce(review.grade, 0)), count(distinct review) from Room room" +
//                " left outer join RoomImage roomImage on roomImage.room = room" +
//                " left outer join Review review on review.room = room group by room, roomImage.imgName")
//        Page<Object[]> getListPage(Pageable pageable);


//    @Query("SELECT room, roomImage FROM Room room" +
//            "LEFT OUTER JOIN ( SELECT roomImage1 FROM RoomImage roomImage1 where roomImage1.inum = (" +
//            "SELECT MIN(roomImage2.inum) FROM RoomImage roomImage2 WHERE roomImage2.rno = roomImage1.rno)" +
//            ") roomImage1 ON room.rno = roomImage1.rno" +
//            " GROUP BY room.rno, roomImage1.inum")
//    List<Object[]> getListPage(Pageable pageable);

//        @Query("select room, roomImage.uuid from Room room" +
//                "left outer join RoomImage roomImage on roomImage.room = room")
        Page<Object[]> getListPage(Pageable pageable);


}
