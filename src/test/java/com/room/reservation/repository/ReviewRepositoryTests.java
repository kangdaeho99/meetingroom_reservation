package com.room.reservation.repository;


import com.room.reservation.entity.Member;
import com.room.reservation.entity.Review;
import com.room.reservation.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReviewRepositoryTests {
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertRoomReviews(){
        //23개 리뷰 등록
        IntStream.rangeClosed(1, 23).forEach( i ->{
            //회의실 번호
            Long rno = (long)(Math.random()*15) + 1;

            //리뷰어 번호
            Long mno = ((long)(Math.random() * 15) + 1);
            Member member = Member.builder().mno(mno).build();

            Review roomReview = Review.builder()
                    .member(member)
                    .room(Room.builder().rno(rno).build())
                    .grade((int)(Math.random()*5) + 1)
                    .text("이 영화에 대한 느낌..." + i)
                    .build();

            reviewRepository.save(roomReview);
        });
    }
    @Test
    public void testGetRoomReviews(){
        Room room = Room.builder().rno(5L).build();

        List<Review> result = reviewRepository.findByRoom(room);

        result.forEach(roomReview ->{
           System.out.println(roomReview.getReviewnum());
            System.out.println(roomReview.getGrade());
            System.out.println(roomReview.getText());
            System.out.println(roomReview.getMember().getEmail());
            System.out.println("----------------------");

        });

    }
}
