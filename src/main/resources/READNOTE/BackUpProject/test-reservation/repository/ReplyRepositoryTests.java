package com.room.reservation.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class ReplyRepositoryTests {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1, 33).forEach(i ->{
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

        IntStream.rangeClosed(1, 33).forEach(i ->{
            long bno = (long)(Math.random() * 8 ) + 1;
//            long bno = i;
            Board board = Board.builder().bno(bno).build();
            Reply reply = Reply.builder()
                    .text("Reply...."+i)
                    .board(board)
                    .replyer("guest")
                    .build();

            replyRepository.save(reply);

        });
    }


    /**
     * Description :
     * 실행된 SQL을 보면 reply, board, member 까지 모두 조인.
     * Reply를 가져올때 매번 Board와 Member까지 조인해서 가져올 필요가 많지않음.
     *
     * fetch는 lazy loading을 권장
     * 위의 쿼리 실행결과와 같이 특정한 엔티티를 조회할때 연관관계를 가진
     * 모든 엔티티를 같이 로딩하는것을 'Eager Loadding' (즉시로딩)
     * JPA에서 연관관계의 데이터를 어떻게 가져올것인가를 fetch(패치)라고 하는데
     * 연관관계의 어노테이션 속성으로 'fetch' 모드를 지정.
     * 'Lazy Loading'으로 처리.(지연 로딩)
     *
     *
     *
     */

    @Test
    public void readReply1(){
        Optional<Reply> result = replyRepository.findById(5L);
        if(result.isPresent()){
            Reply reply = result.get();
            System.out.println(reply);
//            System.out.println(reply.getBoard());
        }
    }

    @Test
    public void testListByBoard(){
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(Board.builder().bno(11L).build());
        replyList.forEach(reply -> System.out.println(reply));
    }
}
