package com.room.reservation.repository;


import com.room.reservation.entity.Member;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1, 15).forEach(i ->{
            Member member = Member.builder()
                    .email("r"+i+"@hello.com")
                    .pw("1111")
                    .nickname("reviewer"+i)
                    .build();
            memberRepository.save(member);
        });
    }

    /**
     * Description : Member 삭제할시 review와 함께 삭제할예정이었으나
     * Room의 writer가 member의 forgein key로 되어있어서
     * del_yn으로 처리할예정
     */
    @Commit
    @Transactional
    @Test
    public void testDeleteMember(){
//        Long mno = 3L;
//        Member member = Member.builder().mno(mno).build();
//
//        reviewRepository.deleteByMember(member);
//        memberRepository.deleteById(mno);
    }
}
