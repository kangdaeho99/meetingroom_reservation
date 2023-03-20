package com.room.reservation.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@RunWith(SpringRunner.class) // 이 RunWith 어노테이션 Null Pointer Exception 을 사라지게 만들어줍니다.
@SpringBootTest
public class MemoRepositoryTests {
    @Autowired
    MemoRepository memoRepository;

    @Test
    public void testClass(){
//        System.out.println(memoRepository.getClass().getName());
        System.out.println(memoRepository.getClass());
    }
}
