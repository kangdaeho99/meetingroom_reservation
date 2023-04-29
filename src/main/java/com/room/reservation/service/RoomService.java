package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;

public interface RoomService {
    void initDataBase();

    Long register(RoomDTO dto);

    PageResultDTO<RoomDTO, Room> getList(PageRequestDTO requestDTO);

    RoomDTO read(Long rno);

    void remove(Long rno);

    void modify(RoomDTO dto);

    default Room dtoToEntity(RoomDTO dto){
        Room entity = Room.builder()
                .rno(dto.getRno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .build();
        return entity;
    }

    default RoomDTO entityToDto(Room entity){
        RoomDTO dto = RoomDTO.builder()
                .rno(entity.getRno())
                .title(entity.getTitle())
                .content(entity.getContent())
                .regDate(entity.getRegDate())
                .modDate(entity.getModDate())
                .build();

        return dto;
    }

}
