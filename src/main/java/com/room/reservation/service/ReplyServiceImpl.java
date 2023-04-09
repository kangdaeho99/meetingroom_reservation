package com.room.reservation.service;

import com.room.reservation.entity.Board;
import com.room.reservation.entity.Reply;
import com.room.reservation.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

/**
 * Description :
 *
 * dtoToEntity(BoardDTO) : DTO가 연관관계를 가진 Board엔티티객체와
 * Member엔티티객체를 구성해야하므로 내부적으로 Member엔티티를 처리하는 과정을 거칩니다.
 *
 *
 */

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository repository;

    @Override
    public void initReplyDataBase() {
        IntStream.rangeClosed(1, 10).forEach(i ->{
            long bno = (long)(Math.random() * 8 ) + 1;
//            long bno = i;
            Board board = Board.builder().bno(bno).build();
            Reply reply = Reply.builder()
                    .text("Reply...."+i)
                    .board(board)
                    .replyer("guest")
                    .build();

            repository.save(reply);

        });
    }
}
