package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Room;
import com.room.reservation.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.function.Function;

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


    /************
     * entityToDto를 이용해 java.util.function을 생성하고 이를 PageResultDTO로 구성합니다.
     * PageResultDTO에는 JPA의 처리결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을 DTO의 리스트로 변환하고,
     * 화면에 페이지 처리와 필요한 값들을 생성합니다.
     * @param requestDTO
     * @return
     */
    @Override
    public PageResultDTO<RoomDTO, Room> getList(PageRequestDTO requestDTO){
        Pageable pageable = requestDTO.getPageable(Sort.by("gno").descending());
        Page<Room> result = repository.findAll(pageable);
        Function<Room, RoomDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);

    }


}
