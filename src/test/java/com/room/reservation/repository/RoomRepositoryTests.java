package com.room.reservation.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.room.reservation.entity.Member;
import com.room.reservation.entity.QRoom;
import com.room.reservation.entity.Room;
import com.room.reservation.entity.RoomImage;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
            Member member = Member.builder().mid((long) i).build();

            Room room = Room.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer(member)
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

        for(Object[] objects : result){
            System.out.println(Arrays.toString(objects));
        }
    }

    @Test
    public void testGetMovieWithAll(){
        List<Object[]> result = roomRepository.getRoomWithAll(5L);
        System.out.println(result);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void updateTest(){
        Optional<Room> result = roomRepository.findById(3L);
        if(result.isPresent()){
            Room room = result.get();

            room.changeTitle("Change Title..");
            room.changeContent("Change Content..");

            roomRepository.save(room);
        }
    }

    @Test
    public void testSearchQuery1(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        QRoom qRoom = QRoom.room;
        String keyword= "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression expression = qRoom.title.contains(keyword);
        builder.and(expression);
        Page<Room> result = roomRepository.findAll(builder, pageable);
        result.stream().forEach(room->{
            System.out.println(room);
        });
    }

    @Test
    public void testSearchQuery2(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        QRoom qRoom = QRoom.room;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qRoom.title.contains(keyword);
        BooleanExpression exContent = qRoom.content.contains(keyword);
        BooleanExpression exAll = exTitle.or(exContent);
        builder.and(exAll);
        builder.and(qRoom.rno.gt(0L));
        Page<Room> result = roomRepository.findAll(builder, pageable);
        result.stream().forEach(room -> {
            System.out.println(room);
        });
    }

    @Transactional
    @Test
    public void testRead1(){
        Optional<Room> result = roomRepository.findById(5L);
        Room room = result.get();
        System.out.println(room);
        System.out.println(room.getWriter());
    }

}
