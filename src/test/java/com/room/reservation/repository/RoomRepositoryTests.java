package com.room.reservation.repository;


import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.room.reservation.entity.QRoom;
import com.room.reservation.entity.Room;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

/**********************
 *
 * @Author 강대호
 * @Description QueryDSL을 활용.(엔티티 클래스에 많은 멤버변수들이 있을경우를 처리하기위함)
 * 상황 설정 : '제목/내용/작성자' 와 같이 단 하나의 항목으로 검색하는경우
 * :'제목 + 내용' / '내용 + 작성자' / '제목 + 작성자' 와 같이 2개의 항목으로 검색하는경우
 * : 제목 + 내용 + 작성자 와 같이 3개의 항목으로 검색하는 경우
 *
 * 사용법 :
 * 1. BooleanBuilder를 생성합니다.
 * 2. 조건에 맞는 구문은 Querydsl에서 사용하는 Predicate 타입의 함수를 생성합니다.
 * 3. BooleanBuilder에 작성된 Predicate를 추가하고 실행합니다.
 *
 *
 ***********************/

@SpringBootTest
public class RoomRepositoryTests {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    public void testQuery1(){
        IntStream.rangeClosed(1, 30).forEach(i ->{
            Room room = Room.builder()
                    .title("Title.."+i)
                    .content("Content..."+i)
                    .writer("user..."+i)
                    .build();
            System.out.println(roomRepository.save(room));
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        //1. 가장 먼저 동적으로 처리하기 위해서 Q도메인 클래스를 얻어옵니다. Q도메인 클래스를 이용하면 엔티티 클래스에 선언된 title, content같은 필드들을 변수로 활용할 수 있습니다.
        QRoom qRoom = QRoom.room;
        String keyword = "1";
        //2. BooleanBuilder는 where문에 들어가는 조건들을 넣어주는 컨테이너라고 간주합니다.
        BooleanBuilder builder = new BooleanBuilder();
        //3. 원하는 조건은 필드값과 같이 결합해서 생성합니다. BooleanBuilder 안에 들어가는 값은 com.querydsl.core.types.Predicate 타입이어야합니다.
        //(Java에 있는 Predicate타입이 아니므로 주의합니다.)
        BooleanExpression expression = qRoom.title.contains(keyword);
        //4. 만들어진 조건은 where문에 and나 or같은 키워드와 결합시킵니다.
        builder.and(expression);
        //5. BooleanBuilder는 RoomRepository에 추가된 QuerydslPredicateExcutor 인터페이스의 findAll()을 사용할 수 있습니다.
        Page<Room> result = roomRepository.findAll(builder,pageable);
        result.stream().forEach(room -> {
            System.out.println(room);
        });

    }

    @Test
    public void testQuery2() {
        IntStream.rangeClosed(1, 30).forEach(i -> {
            Room room = Room.builder()
                    .title("Title.." + i)
                    .content("Content..." + i)
                    .writer("user..." + i)
                    .build();
            System.out.println(roomRepository.save(room));
        });

        Pageable pageable = PageRequest.of(0, 10, Sort.by("gno").descending());
        QRoom qRoom = QRoom.room;
        String keyword = "1";
        BooleanBuilder builder = new BooleanBuilder();
        BooleanExpression exTitle = qRoom.title.contains(keyword);
        BooleanExpression exContent = qRoom.content.contains(keyword);
//        1.exTitle과 exContent라는 Boolean Expression을 결합합니다.
        BooleanExpression exAll = exTitle.or(exContent);
//        2.BooleanBuilder에 추가
        builder.and(exAll);
//        3. 'gno가 0 보다 크다' 라는 조건을 추가합니다.
        builder.and(qRoom.gno.gt(0L));
        Page<Room> result = roomRepository.findAll(builder, pageable);
        result.stream().forEach(room -> {
            System.out.println(room);
        });

    }

    @Test
    public void insertDummies(){
        IntStream.rangeClosed(1, 10).forEach(i -> {
            Room room = Room.builder()
                    .title("Title..." + i)
                    .content("Content ... "+ i)
                    .writer("user" + (i%10))
                    .build();
            System.out.println(roomRepository.save(room));
        });
    }

    @Test
    public void updateTest(){
        IntStream.rangeClosed(1, 20).forEach( i -> {
            Room room = Room.builder()
                    .title("Title..."+ i )
                    .content("Content..."+ i)
                    .writer("user"+(i%10))
                    .build();
            System.out.println(roomRepository.save(room));
        });



        List<Room> roomsList = roomRepository.findAll();
//        Room roomtemp = roomsList.get(0);
//        System.out.println(">>>>>>>>>>> CreateDate="+roomtemp.getRegDate()+" "+roomtemp.getModDate());


        Optional<Room> result = roomRepository.findById(10L);
        if(result.isPresent()){
            Room room = result.get();

            room.changeTitle("Change Title...");
            room.changeContent("Change Content ...");

            roomRepository.save(room);

            result = roomRepository.findById(10L);
            room = result.get();
            System.out.println(">>>>>>>>>>> CreateDate="+room.getRegDate()+" ModdAte"+room.getModDate());
        }
    }

}
