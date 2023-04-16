package com.room.reservation.repository;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

/**
 *
 * Description :
 * 현재 Member, Board, Reply 3개의 테이블이 PK와 FK의 관계.
 * PK쪽에서부터 시작하기 위해 Member 먼저 시작합니다.
 *
 */
@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1, 32).forEach( i -> {
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .password("1111")
                    .name("user"+i)
                    .build();
            memberRepository.save(member);
        });
    }

}
