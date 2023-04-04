package com.room.reservation.repository;


import com.room.reservation.entity.Board;
import com.room.reservation.entity.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.IntStream;

/**
 *
 * Description :
 *
 */
@SpringBootTest
public class BoardRepositoryTests {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;
    /**
     * 한명의 사용자가 하나의 게시물을 등록하도록 합니다.
     *
     */
    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1, 33).forEach(i -> {
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .build();

            memberRepository.save(member);

            Board board = Board.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer(member)
                    .build();

            boardRepository.save(board);

        });
    }

    /**
     * Member를 @ManyToOne으로 참조하고 있는 Board를 조회합니다.
     */
    @Test
    public void testRead1(){

        insertBoard();

        Optional<Board> result = boardRepository.findById(25L);
        if(result.isPresent()){
            Board board = result.get();
            System.out.println(board);
            System.out.println(board.getWriter());
        }

    }
}
