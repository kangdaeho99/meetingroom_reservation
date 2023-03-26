package com.room.reservation.repository;


import com.room.reservation.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 300).forEach(i -> {
            Room room = Room.builder()
                    .title("Title..." + i)
                    .content("Content ... "+ i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(roomRepository.save(room));
        });
    }

}
