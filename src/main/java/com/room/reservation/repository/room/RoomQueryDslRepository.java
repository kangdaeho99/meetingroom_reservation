package com.room.reservation.repository.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomQueryDslRepository {


    Page<Object[]> getListPage(Pageable pageable);
}
