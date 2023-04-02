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

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@Log4j2
@RequiredArgsConstructor //의존성 자동주입을 통해 @Autowired안써도됨
public class RoomServiceImpl implements RoomService{

    private final RoomRepository repository; //반드시 final로 선언

    /**
     * Description : 서버 시작시 database에 기본 데이터값 넣어줍니다.
     *
     */
    @Override
    public void initRoomDataBase(){
        IntStream.rangeClosed(1, 24).forEach(i ->{
            Room room = Room.builder()
                    .title("Title...."+i)
                    .content("Content...."+i)
                    .writer("user..."+i)
                    .build();
            repository.save(room);
        });
    }

    /**
     *
     * 게시글 등록해주는 함수입니다.
     * DTO를 받은뒤 entity로 변경한 후 repository에 해당 entity를 넣어 처리합니다. 그리고 해당 값의 PK를 return합니다.
     * @param dto
     * @return
     */
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

    /**
     *
     * Description : gno의 값을 받아 해당하는 Room정보를 조회함수입니다.
     * findById(gno)를 통해 엔티티 객체를 가져왔다면, entityToDTO()를 이용해서 엔티티객체를 DTO로 변환해서 반환합니다.
     *
     * Optional은 Java 8부터 추가된 클래스로, Null Pointer Exception (NPE)을 피하기 위한 안전한 방법으로 사용됩니다.
     * 예를 들어, repository.findById(gno)에서 데이터가 존재하지 않을 경우 null을 반환하게 되는데,
     * 이 경우 null을 반환하면 NullPointerException이 발생할 가능성이 있습니다.
     * 하지만 Optional을 사용하면 값이 있을 경우 해당 값을, 값이 없을 경우 Optional.empty()를 반환하여 NPE를 예방할 수 있습니다.
     *
     * 따라서, Optional을 사용하면 코드의 안정성이 높아지며, 가독성도 향상됩니다.
     * 또한, 반환 값이 null일 경우 클라이언트에서 해당 예외를 처리하거나, null 대신 기본값을 반환하도록 처리할 수 있습니다.
     *
     * @param gno
     * @return
     */
    @Override
    public RoomDTO read(Long gno) {
        Optional<Room> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }


}
