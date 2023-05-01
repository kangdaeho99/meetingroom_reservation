package com.room.reservation.service;

import com.room.reservation.dto.ReplyDTO;
import com.room.reservation.entity.Reply;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
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

    @Override
    public Long register(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
        return reply.getReplyno();
    }

    @Override
    public List<ReplyDTO> getList(Long rno) {
        List<Reply> result = replyRepository.getRepliesByRoomOrderByReplyno(Room.builder().rno(rno).build());

        return result.stream().map(reply -> entityToDTO(reply)).collect(Collectors.toList());
    }

    @Override
    public void modify(ReplyDTO replyDTO) {
        Reply reply = dtoToEntity(replyDTO);
        replyRepository.save(reply);
    }

    @Override
    public void remove(Long replyno) {
        replyRepository.deleteById(replyno);

    }
}
