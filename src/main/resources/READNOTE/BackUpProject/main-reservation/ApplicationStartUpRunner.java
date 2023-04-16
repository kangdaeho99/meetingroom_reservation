package com.room.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUpRunner implements ApplicationRunner {

    @Autowired
    private MemberService memberService;
    @Autowired
    private BoardService boardService;
    @Autowired
    private ReplyService replyService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        memberService.initMemberDataBase();
        boardService.initBoardDataBase();
        replyService.initReplyDataBase();
    }
}
