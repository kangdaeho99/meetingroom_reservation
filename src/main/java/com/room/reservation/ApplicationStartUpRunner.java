package com.room.reservation;

import com.room.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
//@DependsOn("RoomRepository")
public class ApplicationStartUpRunner implements ApplicationRunner {

//    @Autowired
//    private RoomRepository roomRepository;

    @Autowired
    private RoomService roomService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        IntStream.rangeClosed(1, 24).forEach(i ->{
//            Room room = Room.builder()
//                    .title("Title...."+i)
//                    .content("Content...."+i)
//                    .writer("user..."+i)
//                    .build();
//            roomRepository.save(room);
//        });

        roomService.initRoomDataBase();

    }
}
