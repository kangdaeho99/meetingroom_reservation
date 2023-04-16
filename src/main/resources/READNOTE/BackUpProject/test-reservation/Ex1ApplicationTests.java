package com.room.reservation;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class) // 이 RunWith 어노테이션 Null Pointer Exception 을 사라지게 만들어줍니다.
@SpringBootTest
public class Ex1ApplicationTests {
    @Test
    void contextLoads(){
        System.out.println("context Loads...");
    }
}
