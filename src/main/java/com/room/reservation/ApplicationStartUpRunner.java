package com.room.reservation;

import com.room.reservation.entity.Room;
import com.room.reservation.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.stream.IntStream;

@Component
public class ApplicationStartUpRunner implements ApplicationRunner {

    @Autowired
    private RoomRepository roomRepository;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("helloworld~~~");
        IntStream.rangeClosed(1, 55).forEach(i ->{
            Room room = Room.builder()
                    .title("Title...."+i)
                    .content("Content...."+i)
                    .writer("user..."+i)
                    .build();
            roomRepository.save(room);
        });

    }
}
