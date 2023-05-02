package com.room.reservation.repository.room;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.room.reservation.entity.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.querydsl.jpa.JPAExpressions.select;


@Log4j2
public class RoomQueryDslRepositoryImpl extends QuerydslRepositorySupport implements RoomQueryDslRepository {

    public RoomQueryDslRepositoryImpl() {
        super(Room.class);
    }

    @Override
    public Page<Object[]> getListPageWithReview(Pageable pageable) {
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

    @Override
    public Page<Object[]> getListPageWithReply(Pageable pageable) {
        QRoom room = QRoom.room;
        QRoomImage roomImage = QRoomImage.roomImage;
        QRoomImage roomImage2 = new QRoomImage("roomImage2");
        QReply reply = QReply.reply;
        QReview review = QReview.review;

//        JPQLQuery<Tuple> jpqlQuery = new JPAQuery<>(getEntityManager());

        JPQLQuery<Room> jpqlQuery = from(room);
        jpqlQuery.leftJoin(roomImage).on(room.eq(roomImage.room));
        jpqlQuery.leftJoin(reply).on(room.eq(reply.room));
        jpqlQuery.leftJoin(review).on(room.eq(review.room));
        jpqlQuery.where(roomImage.inum.eq(
                select(roomImage2.inum.min()).from(roomImage2).where(room.eq(roomImage2.room))
                ).or(roomImage.isNull())
        ).groupBy(room);

        JPQLQuery<Tuple> tuple = jpqlQuery.select(room,
                roomImage,
                reply.countDistinct(),
                new CaseBuilder()
                        .when(review.grade.avg().isNull()).then(0.0)
                        .otherwise(review.grade.avg()),
                review.countDistinct());

//        jpqlQuery.select(room, roomImage, reply.countDistinct());
//
//        JPQLQuery<Tuple> tuple = jpqlQuery.select(
//                room,
//                roomImage,
//                reply.countDistinct()
//        ).from(room)
//        .leftJoin(roomImage).on(room.eq(roomImage.room))
//        .leftJoin(reply).on(room.eq(reply.room))
//        .where(
//                roomImage.inum.eq(
//                        select(roomImage2.inum.min()).from(roomImage2).where(room.eq(roomImage2.room))
//                ).or(roomImage.isNull())
//        ).groupBy(room);


        log.info("-------------");
        log.info(tuple);
        log.info("-------------");
        this.getQuerydsl().applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        long count = tuple.fetchCount();
        List<Object[]> collect = result.stream().map(t -> {
            Object[] arr = t.toArray();
            log.info(Arrays.toString(arr));
            return arr;
        }).collect(Collectors.toList());

        return new PageImpl<>(collect, pageable, count);
    }

    @Override
    public Room search1(){
        log.info("search1...........");
        QRoom room = QRoom.room;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Room> jpqlQuery = from(room);
        jpqlQuery.leftJoin(member).on(room.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.room.eq(room));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(room, member, reply.count());
        tuple.groupBy(room);

        log.info("--------------");
        log.info(tuple);
        log.info("--------------");

        List<Tuple> result = tuple.fetch();
        log.info(result);
        return null;
    }

    @Override
    public Page<Object[]> searchPage(String type, String keyword, Pageable pageable) {
        log.info("searchPage..........");
        QRoom room = QRoom.room;
        QReply reply = QReply.reply;
        QMember member = QMember.member;

        JPQLQuery<Room> jpqlQuery = from(room);
        jpqlQuery.leftJoin(member).on(room.writer.eq(member));
        jpqlQuery.leftJoin(reply).on(reply.room.eq(room));

        JPQLQuery<Tuple> tuple = jpqlQuery.select(room, member, reply.count());

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        BooleanExpression expression = room.rno.gt(0L);

        booleanBuilder.and(expression);

        if(type!=null){
            String[] typeArr = type.split("");
            BooleanBuilder conditionBuilder = new BooleanBuilder();
            for(String t:typeArr){
                switch(t){
                    case "t":
                        conditionBuilder.or(room.title.contains(keyword));
                        break;
                    case "w":
                        conditionBuilder.or(member.email.contains(keyword));
                        break;
                    case "c":
                        conditionBuilder.or(room.content.contains(keyword));
                        break;
                }
            }
            booleanBuilder.and(conditionBuilder);
        }
        tuple.where(booleanBuilder);

//        Sort sort = pageable.getSort();
//        sort.stream().forEach(order -> {
//            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
//            String prop = order.getProperty();
//
//            PathBuilder orderByExpression = new PathBuilder(Room.class, "room");
//            tuple.orderBy(new OrderSpecifier<>(direction, orderByExpression.get(prop)));
//        });
//        tuple.groupBy(room);
//
//        //page 처리
//        tuple.offset(pageable.getOffset());
//        tuple.limit(pageable.getPageSize());

        tuple.groupBy(room);
        Objects.requireNonNull(this.getQuerydsl()).applyPagination(pageable, tuple);
        List<Tuple> result = tuple.fetch();
        log.info(result);
        long count = tuple.fetchCount();
        log.info("COUNT: " +count);
        return new PageImpl<Object[]>(
                result.stream().map(Tuple::toArray).collect(Collectors.toList()),
                pageable,
                count);
    }
}
