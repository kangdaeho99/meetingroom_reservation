package com.room.reservation.service;

import com.room.reservation.entity.Room;
import com.room.reservation.entity.RoomImage;
import com.room.reservation.repository.RoomImageRepository;
import com.room.reservation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;

    private final RoomImageRepository imageRepository;

    @Override
    public void initDataBase() {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            Room room = Room.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .build();
            roomRepository.save(room);

            int count = (int)(Math.random() * 5) + 1; //1,2,3,4
            for(int j=0;j<count;j++){
                RoomImage roomImage = RoomImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .imgName("test"+j+".jpg")
                        .room(room)
                        .build();
            imageRepository.save(roomImage);
            }
        });
    }
}
