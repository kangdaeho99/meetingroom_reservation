package com.room.reservation.repository;


import com.room.reservation.entity.Member;
import com.room.reservation.entity.MemberRole;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @DisplayName("Member 테스트데이터 삽입")
    @Test
    public void insertMembers(){

        //1 ~ 10까지는 USER
        //11 ~ 20까지는 USER, ADMIN
        IntStream.rangeClosed(1, 20).forEach(i ->{
            Member member = Member.builder()
                    .email("user"+i+"@hello.com")
                    .password(passwordEncoder.encode("1111"))
                    .nickname("Member"+i)
                    .build();

            //default Role
            member.addMemberRole(MemberRole.USER);

            if( i > 10) member.addMemberRole(MemberRole.ADMIN);

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

    @DisplayName("Member 데이터 조회")
    @Test
    public void testRead(){
        Optional<Member> result = memberRepository.findByEmail("user1@hello.com", false);
        Member member = result.get();
        System.out.println(member);
    }

}
