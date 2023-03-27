package com.room.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/************
 *
 * @Author 강대호
 * @Description
 * PageRequestDTO의 진짜 목적은 JPA쪽에서 사용하는 Pageable 타입의 객체를 생성하는 것
 * 화면에서 전달되는 목록 관련된 데이터에 대한 DTO를 PageRequestDTO라는 이름으로 생성
 * 화면에서 필요한 결과는 PageResultDTO라는 이름의 클래스로 생성
 *
 *
 *
 ***************/

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {
    private int page;
    private int size;

    public PageRequestDTO(){
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort){
        return PageRequest.of(page - 1, size, sort);
    }
}
