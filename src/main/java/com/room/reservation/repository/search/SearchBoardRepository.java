package com.room.reservation.repository.search;

import com.room.reservation.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SearchBoardRepository {
    Board search1();

    /**
     * Description :
     * JPQLQuery로 Page<Object[]> 처리
     * 원하는 파라미터(Pageable)를 전송.
     * 검색타입(type), 키워드(keyword), 페이지정보(Pageable)를 파라미터로 추가합니다.
     * (PageRequestDTO 자체를 파라미터로 처리하지 않은 이유는 DTO를 가능하면 Repository 영역에서 다루지 않기 위함입니다.)
     *
     */
    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);
}
