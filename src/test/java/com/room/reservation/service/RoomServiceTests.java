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
     * @Author 강대호
     * @Description
     * PageRequestDTO를 이용하기 때문에 생성할때는 1페이지부터 처리하고,
     * 정렬은 상황에 맞게 Sort객체를 생성해서 전달.
     * 테스트 코드의 결과를 보면 Page<Room>이 List<RoomDTO>로 변환되어
     * 출력결과에 RoomDTO 타입으로 출력되는것을 볼 수 있습니다.
     *
     ********/
    @Test
    public void testList(){
        IntStream.rangeClosed(1, 50).forEach(i ->{
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
        for(RoomDTO roomDTO : resultDTO.getDtoList()){
            System.out.println(roomDTO);
        }
    }
}
