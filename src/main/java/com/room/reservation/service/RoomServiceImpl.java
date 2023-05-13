package com.room.reservation.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.entity.Member;
import com.room.reservation.entity.QRoom;
import com.room.reservation.entity.Room;
import com.room.reservation.entity.RoomImage;
import com.room.reservation.repository.ReplyRepository;
import com.room.reservation.repository.ReviewRepository;
import com.room.reservation.repository.RoomImageRepository;
import com.room.reservation.repository.RoomRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Log4j2
public class RoomServiceImpl implements RoomService{
    private final RoomRepository roomRepository;

    private final RoomImageRepository roomImageRepository;

    private final ReviewRepository reviewRepository;

    private final ReplyRepository replyRepository;
    @Override
    public void initDataBase() {
        IntStream.rangeClosed(1, 15).forEach(i -> {
            Member member = Member.builder().mno((long) i).build();

            Room room = Room.builder()
                    .title("Title..."+i)
                    .content("Content..."+i)
                    .writer(member)
                    .build();
            roomRepository.save(room);

            int count = (int)(Math.random() * 5) + 1; //1,2,3,4
            for(int j=0;j<count;j++){
                RoomImage roomImage = RoomImage.builder()
                        .uuid(UUID.randomUUID().toString())
                        .imgName("test"+j+".jpg")
                        .room(room)
                        .build();
            roomImageRepository.save(roomImage);
            }
        });
    }

    @Override
    public Long register(RoomDTO dto){
        log.info("DTO-----------");
        log.info(dto);
        Room entity = dtoToEntity(dto);
        log.info(entity);
        roomRepository.save(entity);
        return entity.getRno();
    }

    @Transactional
    @Override
    public Long registerWithRoomImage(RoomDTO roomDTO){
        Map<String, Object> entityMap = dtoToEntityWithRoomImage(roomDTO);
        Room room = (Room) entityMap.get("room");
        List<RoomImage> roomImageList = (List<RoomImage>) entityMap.get("roomImgList");

        roomRepository.save(room);
        if(roomImageList != null){
            roomImageList.forEach(roomImage ->{
                roomImageRepository.save(roomImage);
            });
        }


        return room.getRno();
    }

    /**
     * Description :
     * entityToDto()를 이용해서 java.util.Function을 생성하고 이를 PageResultDTO로 구성
     * PageResultDTO에는 JPA의 처리결과인 Page<Entity>와 Function을 전달해서 엔티티 객체들을
     * DTO의 리스트로 변환하여 화면에 페이지처리와 필요한값들을 생성
     */
    @Override
    public PageResultDTO<RoomDTO, Object[]> getList(PageRequestDTO pageRequestDTO){
        log.info(pageRequestDTO);
        Function<Object[], RoomDTO> fn = (en -> entitiesToDTO(
                (Room) en[0],
                (Member) en[1],
                (List<RoomImage>)Arrays.asList((RoomImage) en[2]),
                (Long) en[3],
                (Double) en[4],
                (Long) en[5]));

//        Page<Object[]> result = roomRepository.getBoardWithReplyCount(pageRequestDTO.getPageable(Sort.by("rno").descending()));


        Page<Object[]> result = roomRepository.searchPageWithImageReplyReview(
                pageRequestDTO.getType(),
                pageRequestDTO.getKeyword(),
                pageRequestDTO.getPageable(Sort.by("rno").descending()));

//        Pageable pageable = pageRequestDTO.getPageable(Sort.by("rno").descending());
//        BooleanBuilder booleanBuilder = getSearch(pageRequestDTO); //검색 조건 처리
//        Page<Room> result = roomRepository.findAll(booleanBuilder, pageable); //querydsl 사용
//        Function<Room, RoomDTO> fn = (entity -> entityToDto(entity));
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public RoomDTO get(Long rno){
        Object result = roomRepository.getRoomByRno(rno);
        Object[] arr = (Object[]) result;
        return entityToDto((Room)arr[0], (Member) arr[1], (Long) arr[2]);
    }

    /**
     * TODO : Room, Review, Member 연관관계 고려하여 삭제처리 다시 하기
     * 고려해야할 것들
     */
    @Transactional
    @Override
    public void remove(Long rno){
        roomRepository.deleteById(rno);
    }

    @Transactional
    @Override
    public void removeWithReplies(Long rno){
        replyRepository.deleteByRno(rno);
        roomRepository.deleteById(rno);
    }

    @Override
    public void modify(RoomDTO dto){
        Optional<Room> result = roomRepository.findById(dto.getRno());
        if(result.isPresent()){
            Room entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            roomRepository.save(entity);
        }
    }

    private BooleanBuilder getSearch(PageRequestDTO requestDTO){ //querydsl 처리
        String type = requestDTO.getType();
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QRoom qRoom = QRoom.room;
        String keyword = requestDTO.getKeyword();
        BooleanExpression expression = qRoom.rno.gt(0L);
        booleanBuilder.and(expression);
        if(type == null || type.trim().length() == 0){ //검색 조건이 없는 경우
            return booleanBuilder;
        }
        //검색조건을 작성하기
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        if(type.contains("t")){
            conditionBuilder.or(qRoom.title.contains(keyword));
        }
        if(type.contains("c")){
            conditionBuilder.or(qRoom.content.contains(keyword));
        }

        //모든 조건 통합
        booleanBuilder.and(conditionBuilder);

        return booleanBuilder;

    }
}
