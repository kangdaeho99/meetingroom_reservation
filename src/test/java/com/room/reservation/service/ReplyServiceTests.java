package com.room.reservation.service;

import com.room.reservation.dto.ReplyDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ReplyServiceTests {

    @Autowired
    private ReplyService replyService;

    @Test
    public void testGetList(){
        Long rno = 5L;
        List<ReplyDTO> replyDTOList = replyService.getList(rno);
        replyDTOList.forEach(replyDTO -> System.out.println(replyDTO));
    }

}
