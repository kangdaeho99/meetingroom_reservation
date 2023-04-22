package com.room.reservation.service;

import com.room.reservation.entity.Member;
import com.room.reservation.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class MemberServiceImpl implements MemberService{
    private final MemberRepository memberRepository;


    @Override
    public void initDataBase() {
        IntStream.rangeClosed(1, 15).forEach(i ->{
            Member member = Member.builder()
                    .email("r"+i+"@hello.com")
                    .pw("1111")
                    .nickname("reviewer"+i)
                    .build();
            memberRepository.save(member);
        });
    }
}
