package com.room.reservation;

import com.room.reservation.service.MemberService;
import com.room.reservation.service.ReplyService;
import com.room.reservation.service.ReviewService;
import com.room.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUpRunner implements ApplicationRunner {

    @Autowired
    private RoomService roomService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReplyService replyService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberService.initDataBase();
        roomService.initDataBase();
        reviewService.initDataBase();
        replyService.initDataBase();
    }
}
