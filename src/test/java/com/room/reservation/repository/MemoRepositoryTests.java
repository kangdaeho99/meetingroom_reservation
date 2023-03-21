package com.room.reservation.repository;


import com.room.reservation.entity.Memo;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

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

/*************************************/
/*************************************/
/**********H2로 테스트할때**************/
/*************************************/
/*************************************/
    @After
    public void cleanup(){
        memoRepository.deleteAll();
    }

    @Test
    public void 게시글저장_불러오기(){
        //given
        String title="테스트게시글";
        String content ="테스트본문";

//      PostRepository.save
//      - 테이블 posts에 insert/update 쿼리를 실행합니다.
//      - id값이 있다면 update, 없다면 insert 쿼리가 실행됩니다.
        memoRepository.save(Memo.builder()
                .memoText(content)
                .build());


        //when
//      PostRepsotory.findAll
//        -테이블 Posts에 있는 모든 데이터를 조회해오는 메소드입니다.
        List<Memo> memosList = memoRepository.findAll();

        //then
        Memo memos = memosList.get(0);
        assertThat(memos.getMemoText()).isEqualTo(content);
    }







/*************************************/
/*************************************/
/**********DB로 테스트할때**************/
/*************************************/
/*************************************/
//    @Test
//    public void testInsertDummies(){
//        IntStream.rangeClosed(1,100).forEach(i -> {
//            Memo memo = Memo.builder().memoText("Sample..."+i).build();
//            memoRepository.save(memo);
//        });
//    }
//
//    @Test
//    public void testSelect(){
//        Long mno = 100L;
//        Optional<Memo> result = memoRepository.findById(mno);
//        System.out.println("==========================");
//        if(result.isPresent()){
//            Memo memo = result.get();
//            System.out.println(memo);
//        }
//    }
//
//    @Transactional
//    @Test
//    public void testSelect2(){
//        Long mno = 100L;
//        Memo memo = memoRepository.getOne(mno);
//        System.out.println("===================");
//        System.out.println(memo);
//    }
//
//    @Test
//    public void testUpdate(){
//        Memo memo = Memo.builder().mno(100L).memoText("Update Text").build();
//        System.out.println(memoRepository.save(memo));
//    }
//
//    @Test
//    public void testDelete(){
//        Long mno = 100L;
//        memoRepository.deleteById(mno);
//    }
}
