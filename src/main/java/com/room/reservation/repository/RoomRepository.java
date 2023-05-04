package com.room.reservation.repository;

import com.room.reservation.entity.Room;
import com.room.reservation.repository.room.RoomQueryDslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long>, RoomQueryDslRepository, QuerydslPredicateExecutor<Room> {
    /**
     * Mysql 특징으로 RoomImage를 사용할시 에러. QueryDSL으로 대체
     */
//    Page<Object[]> getListPage(Pageable pageable);

    @Query("SELECT r, ri, avg(coalesce(rv.grade,0)), count(distinct rv)" +
            " from Room r " +
            " left outer join RoomImage ri on ri.room = r" +
            " left outer join Review rv on rv.room = r" +
            " where r.rno = :rno" +
            " group by ri")
    List<Object[]> getRoomWithAll(Long rno);

    /**
     * Description : 엔티티 클래스 내부에 연관관계가 있는경우
     * Room Entity의 내부에는 Member Entity 클래스를 변수로 선언하고, 연관관계를 맺고 있음.
     * 이러한 경우에 Room의 Writer 변수를 이용해서 처리함
     * Board를 가지고오지만, Member를 같이 조회해야하는 상황.
     * 이처럼 내부에 있는 엔티티를 이용할때는 LEFT JOIN 뒤에 ON을 사용하는 부분이 없음.
     * 한개의 Object 내에 Object[] 로 나옵니다.
     */
    @Query("select r, w from Room r left join r.writer w where r.rno = :rno")
    Object getRoomWithWriter(@Param("rno") Long rno);

    /**
     * Description : 연관관게가 없는 엔티티 조인처리에는 on 을 사용
     * Room과 Member 사이에는 내부적으로 참조를 통해서 연관관계가 있지만,
     * Room과 Reply는 상황이 다릅니다. Reply 쪽이 @ManyToOne으로 참조하고 있으나,
     * Room 입장에서는 Reply 객체들을 참조하고 있지 않기 떄문에 문제가됩니다.
     * 이런 경우에는 직접 조인에 필요한 조건을 'on'을 이용하여 처리합니다.
     * '특정 게시물과 해당 게시물에 속한 댓글들을 조회' 할 경우 아래처럼 처리합니다.
     */
    @Query("SELECT r, rp from Room r LEFT JOIN Reply rp ON rp.room = r WHERE r.rno = :rno")
    List<Object[]> getRoomWithReply(@Param("rno") Long rno);

    @Query(value="SELECT r, w, count(r) " +
            " FROM Room r " +
            " LEFT JOIN r.writer w" +
            " LEFT JOIN Reply rp ON rp.room = r" +
            " GROUP BY r",
    countQuery = "SELECT count(r) FROM Room r")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);

    @Query("SELECT r, w, count(rp) " +
            " FROM Room r LEFT JOIN r.writer w " +
            " LEFT OUTER JOIN Reply rp ON rp.room = r" +
            " WHERE r.rno = :rno" )
    Object getRoomByRno(@Param("rno") Long rno);

    /**
     * Description : Room 조회할시 RoomImage, Reply, Review를 모두 Join하여 가져오는 Query입니다.
     */
}
