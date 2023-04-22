package com.room.reservation.repository;

import com.room.reservation.entity.Room;
import com.room.reservation.entity.RoomImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private RoomImageRepository imageRepository;

    @Commit
    @Transactional
    @Test
    public void insertRooms(){
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

    @Test
    public void testListPage(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rno"));

        Page<Object[]> result = roomRepository.getListPage(pageRequest);

//        for(Object[] objects : result){
//            System.out.println(Arrays.toString(objects));
//        }

//        for(Object[] objects : result.getContent()){
//            System.out.println(Arrays.toString(objects));
//        }


    }
}
