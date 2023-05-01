package com.room.reservation.service;

import com.room.reservation.dto.ReplyDTO;
import com.room.reservation.entity.Reply;
import com.room.reservation.entity.Room;

import java.util.List;

public interface ReplyService {
    void initDataBase();

    Long register(ReplyDTO replyDTO);

    List<ReplyDTO> getList(Long rno);

    void modify(ReplyDTO replyDTO);

    void remove(Long replyno);

    //ReplyDTO를 Reply 객체로 변환 Room 객체의 처리가 수반
    default Reply dtoToEntity(ReplyDTO replyDTO){
        Room room = Room.builder()
                .rno(replyDTO.getRno())
                .build();

        Reply reply = Reply.builder()
                .replyno(replyDTO.getReplyno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .room(room)
                .build();

        return reply;
    }

    //Reply 객체를 ReplyDTO로 변환 Room 객체가 필요하지 않으므로 게시물 번호만
    default ReplyDTO entityToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .replyno(reply.getReplyno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();

        return dto;
    }
}
