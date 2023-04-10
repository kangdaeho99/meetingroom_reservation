package com.room.reservation.service;

import com.room.reservation.dto.ReplyDTO;
import com.room.reservation.entity.Board;
import com.room.reservation.entity.Reply;

import java.util.List;

/**
 * Description :
 *
 * dtoToEntity(BoardDTO) : DTO가 연관관계를 가진 Board엔티티객체와
 * Member엔티티객체를 구성해야하므로 내부적으로 Member엔티티를 처리하는 과정을 거칩니다.
 *
 *
 */
public interface ReplyService {
    void initReplyDataBase();

    Long register(ReplyDTO replyDTO);
    List<ReplyDTO> getList(Long bno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);

    //ReplyDTO를 Reply 객체로 변환 Board 객체의 처리가 수반됨
    default Reply dtoToEntity(ReplyDTO replyDTO){
        Board board = Board.builder().bno(replyDTO.getBno()).build();
        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replyer(replyDTO.getReplyer())
                .board(board)
                .build();

        return reply;
    }

    //Reply 객체를 ReplyDTO로 변환 Board객체가 필요하지 않으므로 게시물 번호만
    default ReplyDTO entityToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replyer(reply.getReplyer())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                .build();
        return dto;
    }
}
