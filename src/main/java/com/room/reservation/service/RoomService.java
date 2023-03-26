package com.room.reservation.service;

import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;

public interface RoomService {
    Long register(RoomDTO dto);

    default Room dtoToEntity(RoomDTO dto){
        Room entity = Room.builder()
                .gno(dto.getGno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(dto.getWriter())
                .build();
        return entity;
    }
}
