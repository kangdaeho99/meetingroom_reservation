package com.room.reservation.service;

import com.room.reservation.dto.BoardDTO;
import com.room.reservation.entity.Board;
import com.room.reservation.entity.Member;

/**
 * Description :
 *
 * dtoToEntity(BoardDTO) : DTO가 연관관계를 가진 Board엔티티객체와
 * Member엔티티객체를 구성해야하므로 내부적으로 Member엔티티를 처리하는 과정을 거칩니다.
 *
 *
 */
public interface BoardService {
    Long register(BoardDTO dto);

    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder().email(dto.getWriterEmail()).build();

        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();

        return board;
    }
}
