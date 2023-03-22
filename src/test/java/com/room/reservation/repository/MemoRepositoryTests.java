package com.room.reservation.repository;


import com.room.reservation.entity.Memo;
import jakarta.transaction.Transactional;
import org.junit.After;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Commit;

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

    @Test
    public void testPageDefault(){
        //given
        String content ="테스트본문";

        for(int i=0;i<99;i++){
            memoRepository.save(Memo.builder()
                    .memoText(content+" "+i)
                    .build());
        }


        //1페이지 10개
        Pageable pageable = PageRequest.of(0, 10);
        Page<Memo> result = memoRepository.findAll(pageable);
        System.out.println(result);
        System.out.println("=========================================");
        System.out.println("Total Pages: "+result.getTotalPages()); //총 몇페이지
        System.out.println("Total Count: "+result.getTotalElements()); //전체 개수
        System.out.println("Page Number: "+result.getNumber()); //현재 페이지 번호
        System.out.println("Page Size: "+result.getSize()); //페이지당 데이터 개수
        System.out.println("has next page?: "+result.hasNext()); //다음 페이지
        System.out.println("first Page: "+result.isFirst()); //시작페이지(0) 여부

        for(Memo memo : result.getContent()){
            System.out.println(memo);
        }

    }

    @Test
    public void testSort(){
        //given
        String content ="테스트본문";

        for(int i=0;i<10;i++){
            memoRepository.save(Memo.builder()
                    .memoText(content+" "+i)
                    .build());
        }

        Sort sort1 = Sort.by("mno").descending();
        Pageable pageable = PageRequest.of(0, 10, sort1);
        Page<Memo> result = memoRepository.findAll(pageable);

        result.get().forEach(memo ->{
            System.out.println(memo);
        });

    }

    @Test
    public void testQueryMethods(){
        //given
        String content ="테스트본문";
        for(int i=0;i<99;i++){
            memoRepository.save(Memo.builder()
                    .memoText(content+" "+i)
                    .build());
        }

        List<Memo> list = memoRepository.findByMnoBetweenOrderByMnoDesc(70L, 80L);
        for(Memo memo : list){
            System.out.println(memo);
        }
    }

    @Test
    public void testQueryMethodWithPageable(){
        //given
        String content ="테스트본문";
        for(int i=0;i<99;i++){
            memoRepository.save(Memo.builder()
                    .memoText(content+" "+i)
                    .build());
        }

        Pageable pageable = PageRequest.of(0, 10, Sort.by("mno").descending());
        Page<Memo> result = memoRepository.findByMnoBetween(10L, 50L, pageable);
        result.get().forEach(memo -> System.out.println(memo));
    }


    /****************
    @Author 강대호
     @Description deleteby 인경우 우선 'select'문으로 해당 엔티티객체들을 가져오는 작업과 각 엔티티를 삭제하는 작업이 같이 이루어지기에
     Commit과 Transactional 어노테이션이 사용됩니다.
    Commit을 사용하여야 최종결과 커밋, Transacitonal 사용안할시 에러. deleteby는 한번에 삭제가 이루어지지않고 각 엔티티객체를 하나씩 삭제
     하기에 잘사용되지않음 개발시에는 @Query를 이용해 개선.
    *********/
    @Commit
    @Transactional
    @Test
    public void testDeleteQueryMethods(){
        String content="테스트본문";
        for(int i=0;i<99;i++){
            memoRepository.save(Memo.builder().memoText(content).build());
        }

        memoRepository.deleteMemoByMnoLessThan(10L);
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
