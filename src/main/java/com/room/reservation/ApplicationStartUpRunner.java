package com.room.reservation;

import com.room.reservation.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationStartUpRunner implements ApplicationRunner {

    @Autowired
    private RoomService roomService;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        roomService.initRoomDataBase();

    }
}
