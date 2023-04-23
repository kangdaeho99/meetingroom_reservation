package com.room.reservation.repository.room;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.room.reservation.entity.QReview;
import com.room.reservation.entity.QRoom;
import com.room.reservation.entity.QRoomImage;
import com.room.reservation.entity.Room;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;


@Log4j2
public class RoomQueryDslRepositoryImpl extends QuerydslRepositorySupport implements RoomQueryDslRepository {

    public RoomQueryDslRepositoryImpl() {
        super(Room.class);
    }

    @Override
    public Page<Object[]> getListPage(Pageable pageable) {
            QRoom room = QRoom.room;
            QRoomImage roomImage = QRoomImage.roomImage;
            QRoomImage roomImage2 = new QRoomImage("roomImage2");
            QReview review = QReview.review;

            JPQLQuery<Tuple> jpqlQuery = new JPAQuery<>(getEntityManager());

            JPQLQuery<Tuple> tuple = jpqlQuery.select(
                    room,
                    roomImage,
                    new CaseBuilder()
                            .when(review.grade.avg().isNull()).then(0.0)
                            .otherwise(review.grade.avg()),
                    review.countDistinct()
            ).from(room)
            .leftJoin(roomImage).on(room.eq(roomImage.room))
            .leftJoin(review).on(room.eq(review.room))
            .where(
                    roomImage.inum.eq(
                            select(roomImage2.inum.min()).from(roomImage2).where(room.eq(roomImage2.room))
                    ).or(roomImage.isNull())

            ).groupBy(room);




//        tuple.orderBy(roomImage.inum.desc());

        log.info("---------------");
        log.info(tuple);
        log.info("---------------");
        this.getQuerydsl().applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();
        List<Object[]> collect = result.stream().map(t ->{
            Object[] arr = t.toArray();
            log.info(Arrays.toString(arr));
            return arr;
        }).collect(Collectors.toList());

//        for(int i=0;i<result.size();i++){
//            System.out.println(result.get(i));
//        }
        return new PageImpl<>(collect, pageable, count);

    }
}
