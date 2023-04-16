package com.room.reservation.repository;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;
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
     * Lazy Loading으로 지정한다면 데이터베이스와 추가적 연결이 필요하다는 오류(could not initalize proxy)
     * Lazy Loading이기에 board테이블만을 가져와서 System.out.println()은 상관없지만
     * board.getWriter()에서 문제가 납니다.
     * board.getWriter()는 member테이블을 로딩해야하는데 이미 데이터베이스와의 연결이 끝난상태
     * 'no Session' 에러가 나옵니다.
     * 이떄 데이터베이스와의 연결이 한번더 필요한데 @Transactional 로 처리합니다.
     * 실행결과 : 처음에는 board테이블만 로딩, board.getWriter를 처리하기 위해 member테이블 추가로딩
     * 조인으로 처리된것과 다른것을 확인할 수 있습니다.
     *
     * 연관관계에서는 @ToString()을 주의.
     */

    @Transactional
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

    /**
     * Lazy Loading으로 처리되었지만 실행되는 쿼리를 보면 조인처리가 되어
     * board테이블과 member테이블을 사용하고 있습니다.
     */
    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(5L);

        Object[] arr = (Object[]) result;

        System.out.println("---------------------");
        System.out.println(Arrays.toString(arr));
    }

    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(5L);
        for(Object[] arr: result){
            System.out.println(Arrays.toString(arr));
        }
    }

    @Test
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());
        Page<Object[]> result = boardRepository.getBoardwithReplyCount(pageable);
        result.get().forEach(row -> {
            Object[] arr = (Object[]) row;
            System.out.println(Arrays.toString(arr));
        });
    }

    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(5L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));

    }

    /**
     * Description :
     * SQL을 실행하지는 않으니
     * search1 ------------------ 와 같은 로그가 실행되는지 확인합니다.
     *
     * 실행되는 로그를 보면
     */
    @Test
    public void testSearch1(){
        boardRepository.search1();
    }

    /**
     * Description :
     * 테스트 코드에서 고의적으로 중첩되는 Sort 조건을 만들었습니다.
     * 실행결과를 보면 order by 조건, 목록을 위한 SQL과 count 처리를 위한 SQL이 실행된것을 확인가능
     */
    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0,10,
                Sort.by("bno").descending()
                        .and(Sort.by("title").ascending()));
        Page<Object[]> result = boardRepository.searchPage("t","1",pageable);
    }

}
