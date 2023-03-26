package com.room.reservation.service;

import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동주입을 통해 @Autowired안써도됨
public class RoomServiceImpl implements RoomService{

    private final RoomRepository repository; //반드시 final로 선언

    @Override
    public Long register(RoomDTO dto) {
        log.info("DTO---------------");
        log.info(dto);
        Room entity = dtoToEntity(dto);
        log.info(entity);
        repository.save(entity);
        return entity.getGno();
    }



}
