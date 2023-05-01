package com.room.reservation.repository;

import com.room.reservation.entity.Reply;
import com.room.reservation.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1, 15).forEach(i -> {
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

    @Test
    public void readReply1(){
//        Optional<Reply> result = replyRepository.findById(1L);
//        Reply reply = result.get();
//        System.out.println(reply);
//        System.out.println(reply.getRoom());
    }

    @Test
    public void testListByRoom(){
        List<Reply> replyList = replyRepository.getRepliesByRoomOrderByReplyno(Room.builder().rno(3L).build());
        replyList.forEach(reply -> System.out.println(reply));
    }
}
