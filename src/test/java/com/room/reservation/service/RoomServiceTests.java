package com.room.reservation.service;


import com.room.reservation.dto.RoomDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomServiceTests {
    @Autowired
    private RoomService service;

    @Test
    public void testRegister(){
        RoomDTO roomDTO = RoomDTO.builder()
                .title("SAMPLE TITLE....")
                .content("Sample Content....")
                .writer("user0")
                .build();
        System.out.println(service.register(roomDTO));

    }
}
