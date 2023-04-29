package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;
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
                .title("Sample TItle...")
                .content("Sample Content ...")
                .build();

        service.register(roomDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
        PageResultDTO<RoomDTO, Room> resultDTO = service.getList(pageRequestDTO);
        System.out.println("PREV : "+resultDTO.isPrev());
        System.out.println("NEXT : "+resultDTO.isNext());
        System.out.println("TOTAL : "+resultDTO.getTotalPage());
        System.out.println("----------------------------");
        for(RoomDTO roomDTO : resultDTO.getDtoList()){
            System.out.println(roomDTO);
        }
        System.out.println("----------------------------");
        resultDTO.getPageList().forEach(i -> System.out.println(i));
        System.out.println(resultDTO.getSize());
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("5")
                .build();
        PageResultDTO<RoomDTO, Room> resultDTO  = service.getList(pageRequestDTO);
        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: "+resultDTO.getTotalPage());
        System.out.println("-----------------------------");
        for(RoomDTO roomDTO : resultDTO.getDtoList()){
            System.out.println(roomDTO);
        }
        System.out.println("=============================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));

    }
}
