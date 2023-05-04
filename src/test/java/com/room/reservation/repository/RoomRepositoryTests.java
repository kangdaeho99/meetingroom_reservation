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
    private RoomImageRepository roomImageRepository;


    @Commit
    @Transactional
    @Test
    public void insertRooms(){
        IntStream.rangeClosed(1, 15).forEach(i -> {
            Member member = Member.builder().mno((long) i).build();

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
                roomImageRepository.save(roomImage);
            }
        });
    }

    @Test
    public void testListPage(){
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "rno"));

        Page<Object[]> result = roomRepository.getListPageWithReply(pageRequest);

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

    @Test
    public void testReadWithWriter(){
        Object result = roomRepository.getRoomWithWriter(5L);
        Object[] arr = (Object[]) result;
        System.out.println("--------------");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = roomRepository.getRoomWithReply(5L);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Page<Object[]> result = roomRepository.getBoardWithReplyCount(pageable);
        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });

    }

    @Test
    public void testRead3(){
        Object result = roomRepository.getRoomByRno(5L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testSearch1(){
        roomRepository.search1();
    }

    @Test
    public void testSearchPage(){
//        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending());
        Pageable pageable = PageRequest.of(0, 10, Sort.by("rno").descending()
                .and(Sort.by("title").ascending()));
        Page<Object[]> result = roomRepository.searchPage("t", "1", pageable);
    }

    @Commit
    @Transactional
    @Test
    public void insertMovies(){
        IntStream.rangeClosed(1, 15).forEach( i -> {
            Room room = Room.builder()
                    .title("Room..."+i)
                    .content("Content..."+i)
                    .build();
            System.out.println("-----------------------");
            roomRepository.save(room);
            int count = (int)(Math.random() * 3) + 1;
            for(int j=0;j<count;j++){
                RoomImage roomImage = RoomImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .room(room)
                        .imgName("Test"+j+".jpg").build();

                roomImageRepository.save(roomImage);
            }
        });
    }

    @Test
    public void testGetRoomWithAll(){
        List<Object[]> result = roomRepository.getRoomWithAll(5L);
        System.out.println(result);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
        }
    }

}
