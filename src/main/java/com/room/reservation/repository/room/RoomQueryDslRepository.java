package com.room.reservation.repository.room;

import com.room.reservation.entity.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomQueryDslRepository {


    Page<Object[]> getListPageWithReview(Pageable pageable);

    /**
     * @Description : Room 객체, RoomImage 객체, Reply 객체(댓글 개수), Review 객체(리뷰 평균, 리뷰 개수)를 모두 가져옵니다.
     */
    Page<Object[]> getListPageWithReply(Pageable pageable);

    Room search1();

    Page<Object[]> searchPage(String type, String keyword, Pageable pageable);

    Page<Object[]> searchPageWithImageReplyReview(String type, String keyword, Pageable pageable);

}
