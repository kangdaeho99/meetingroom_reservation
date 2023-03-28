package com.room.reservation.service;


import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.RoomRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
public class RoomServiceTests {
    @Autowired
    private RoomService service;

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testRegister(){
        RoomDTO roomDTO = RoomDTO.builder()
                .title("SAMPLE TITLE....")
                .content("Sample Content....")
                .writer("user0")
                .build();
        System.out.println(service.register(roomDTO));

    }

    /*********
     *
     * Author 강대호
     * Description
     * PageRequestDTO를 이용하기 때문에 생성할때는 1페이지부터 처리하고,
     * 정렬은 상황에 맞게 Sort객체를 생성해서 전달.
     * 테스트 코드의 결과를 보면 Page<Room>이 List<RoomDTO>로 변환되어
     * 출력결과에 RoomDTO 타입으로 출력되는것을 볼 수 있습니다.
     *
     ********/
    @Test
    public void testList(){
        IntStream.rangeClosed(1, 102).forEach(i ->{
            Room room = Room.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer("user..."+i)
                    .build();
            System.out.println(roomRepository.save(room));
        });

        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResultDTO<RoomDTO, Room> resultDTO = service.getList(pageRequestDTO);
        
//        PREV: false 현재 1페이지이니 PREV가 없어야됨
//        NEXT: true 현재 총 102페이지이니 NEXT있어야됨
//        TOTAL: 11 102개이니 총 11페이지까지 존재, 전체페이지의 개수
        System.out.println("PREV: "+resultDTO.isPrev());
        System.out.println("NEXT: "+resultDTO.isNext());
        System.out.println("TOTAL: "+resultDTO.getTotalPage());
        System.out.println("=======================================");

        for(RoomDTO roomDTO : resultDTO.getDtoList()){
            System.out.println(roomDTO);
        }
//        ======================================= 화면에 출력될 페이지번호
//        1
//        2
//        3
//        4
//        5
//        6
//        7
//        8
//        9
//        10
        System.out.println("=======================================");
        resultDTO.getPageList().forEach(i -> System.out.println(i));


    }
}
