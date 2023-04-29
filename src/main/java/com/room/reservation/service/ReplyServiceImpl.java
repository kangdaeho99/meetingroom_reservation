package com.room.reservation.service;

import com.room.reservation.entity.Reply;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class ReplyServiceImpl implements ReplyService{
    private final ReplyRepository replyRepository;


    @Override
    public void initDataBase() {
        IntStream.rangeClosed(1, 20).forEach( i ->{
            long rno = (long)(Math.random() * 15) + 1;
            Room room = Room.builder().rno(rno).build();

            Reply reply = Reply.builder()
                    .text("Reply...." + i)
                    .room(room)
                    .replyer("Guest")
                    .build();
            replyRepository.save(reply);
        });
    }
}
