package com.room.reservation.service;

import com.room.reservation.entity.Member;
import com.room.reservation.entity.Review;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReviewServiceImpl implements ReviewService{
    private final ReviewRepository reviewRepository;

    @Override
    public void initDataBase() {
        //23개 리뷰 등록
        IntStream.rangeClosed(1, 23).forEach( i ->{
            //회의실 번호
            Long rno = (long)(Math.random()*15) + 1;

            //리뷰어 번호
            Long mid = ((long)(Math.random() * 15) + 1);
            Member member = Member.builder().mid(mid).build();

            Review roomReview = Review.builder()
                    .member(member)
                    .room(Room.builder().rno(rno).build())
                    .grade((int)(Math.random()*5) + 1)
                    .text("이 영화에 대한 느낌..." + i)
                    .build();

            reviewRepository.save(roomReview);
        });
    }
}
