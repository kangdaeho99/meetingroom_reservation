package com.room.reservation.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Description
 * JPA를 이용하는 Repository에서 페이지 처리 결과를 Page<Entity> 타입으로 반환합니다.
 * 서비스 계층에서 처리하기 위해
 * -Page<Entity>의 엔티티 객체들을 DTO 객체로 변환해서 list에 담아줍니다.
 * -화면 출력에 필요한 정보 구성
 *
 * PageResultDTO 클래스는 다양한 곳에서 사용하기 위해 제너릭 타입을 이용합니다.
 * DTO, EN이라는 타입으로 사용
 *
 * PageResultDTO는 Page<Entity> 타입을 이용해서 생성할 수 있도록 생성자로 작성합니다
 * - Function<EN,DTO>는 엔티티 객체들을 DTO로 변환해주는 기능입니다.
 * 나중에 어떤 종류의 Page<E>타입이 생성되더라도 PageResultDTO로 처리가능합니다.
 *
 * PageResultDTO는 List<DTO> 타입으로 DTO 객체들을 보관합니다.
 * 그렇다면 Page<Entity> 내용물 중에서 엔티티 객체를 DTO로 변환하는 기능이 필요합니다.
 * 가장 일반적인 형태는 추상클래스를 이용해 이를 처리해야하는 방식이지만
 * 매번 새로운 클래스가 필요하다는 단점이 있습니다.
 *
 * 엔티티 객체의 DTO 변환은 서비스 인터페이스에 정의한 메서드(entityToDto())와 별도의 Function 객체로 만들어서 처리합니다.
 *
 * @param <DTO>
 * @param <EN>
 */
@Data
public class PageResultDTO<DTO, EN> {
    //DTO리스트
    private List<DTO> dtoList;

    //총 페이지 번호
    private int totalPage;

    //현재 페이지 번호
    private int page;
    //목록 사이즈
    private int size;

    //시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    //이전, 다음
    private boolean prev, next;

    //페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn){
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        this.page = pageable.getPageNumber() + 1; //0부터 시작하므로 1을 추가
        this.size = pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) * 10;
        start = tempEnd - 9;
        prev = start > 1;
        end = totalPage > tempEnd ? tempEnd : totalPage;
        next = totalPage > tempEnd;
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());

    }
}
