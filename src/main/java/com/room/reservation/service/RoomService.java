package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Member;
import com.room.reservation.entity.Room;

public interface RoomService {
    void initDataBase();

    Long register(RoomDTO dto);

    PageResultDTO<RoomDTO, Object[]> getList(PageRequestDTO requestDTO);

    RoomDTO get(Long rno);

    void remove(Long rno);

    void removeWithReplies(Long rno);

    void modify(RoomDTO dto);

    default Room dtoToEntity(RoomDTO dto){
        Member member = Member.builder().mno(dto.getWriterMno()).build();

        Room entity = Room.builder()
                .rno(dto.getRno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return entity;
    }

    /**
     * Description :
     * Object[]을 DTO로 변환
     * JPQL의 결과로 나오는 Object[]을 DTO 타입으로 변환하는 기능
     * 이 기능은 Java.util.Function을 이용해서 작성.
     * 현재 예제의 경우 JPQL의 실행결과로 나오는 Object[]을 RoomDTO로 변환.
     * Object[]의 내용은 Room, Member, 댓글의 수는 Long타입으로 나오게 되므로 이를 파라미터로
     * 전달받아서 RoomDTO를 구성하도록 작성함.
     */
    default RoomDTO entityToDto(Room room, Member member, Long replyCount){

        RoomDTO dto = RoomDTO.builder()
                .rno(room.getRno())
                .title(room.getTitle())
                .content(room.getContent())
                .regDate(room.getRegDate())
                .modDate(room.getModDate())
                .writerMno(member.getMno())
                .writerEmail(member.getEmail())
                .writerName(member.getNickname())
                .replyCount(replyCount.intValue())
                .build();

        return dto;
    }

}
