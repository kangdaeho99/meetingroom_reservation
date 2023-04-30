package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RoomServiceTests {

    @Autowired
    private RoomService roomService;

    @Test
    public void testRegister(){
        RoomDTO roomDTO = RoomDTO.builder()
                .title("Sample TItle...")
                .content("Sample Content ...")
                .writerMno((long) 3)
                .build();

        roomService.register(roomDTO);
    }

    @Test
    public void testList(){
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        PageResultDTO<RoomDTO, Object[]> result = roomService.getList(pageRequestDTO);
        for(RoomDTO roomDTO : result.getDtoList()){
            System.out.println(roomDTO);
        }
//        PageRequestDTO pageRequestDTO = PageRequestDTO.builder().page(1).size(10).build();
//        PageResultDTO<RoomDTO, Room> resultDTO = roomService.getList(pageRequestDTO);
//        System.out.println("PREV : "+resultDTO.isPrev());
//        System.out.println("NEXT : "+resultDTO.isNext());
//        System.out.println("TOTAL : "+resultDTO.getTotalPage());
//        System.out.println("----------------------------");
//        for(RoomDTO roomDTO : resultDTO.getDtoList()){
//            System.out.println(roomDTO);
//        }
//        System.out.println("----------------------------");
//        resultDTO.getPageList().forEach(i -> System.out.println(i));
//        System.out.println(resultDTO.getSize());
    }

    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc")
                .keyword("5")
                .build();
        PageResultDTO<RoomDTO, Object[]> resultDTO  = roomService.getList(pageRequestDTO);
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

    @Test
    public void testGet(){
        Long rno = 5L;
        RoomDTO roomDTO = roomService.get(rno);
        System.out.println(roomDTO);
    }

    @Test
    public void testRemove(){
        Long rno = 2L;
//        roomService.removeWithReplies(rno);
    }

    @Test
    public void testModify(){
        RoomDTO roomDTO = RoomDTO.builder()
                .rno(5L)
                .title("Change TItle!")
                .content("Change Content!")
                .build();

        roomService.modify(roomDTO);
    }
}
