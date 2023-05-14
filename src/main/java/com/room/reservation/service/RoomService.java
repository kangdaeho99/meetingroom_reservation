package com.room.reservation.service;

import com.room.reservation.dto.PageRequestDTO;
import com.room.reservation.dto.PageResultDTO;
import com.room.reservation.dto.RoomDTO;
import com.room.reservation.dto.RoomImageDTO;
import com.room.reservation.entity.Member;
import com.room.reservation.entity.Room;
import com.room.reservation.entity.RoomImage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface RoomService {
    void initDataBase();

    Long register(RoomDTO dto);

    Long registerWithRoomImage(RoomDTO dto);

    PageResultDTO<RoomDTO, Object[]> getList(PageRequestDTO requestDTO);

    RoomDTO get(Long rno);

    RoomDTO getRoomWithMemberImageReview(Long rno);

    void remove(Long rno);

    void removeWithReplies(Long rno);

    void modify(RoomDTO dto);

    default Room dtoToEntity(RoomDTO dto){
        Member member = Member.builder().mno(dto.getWriterMno()).build();

        Room entity = Room.builder()
                .rno(dto.getRno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return entity;
    }

    /**
     * Description : Roomm RoomImage을 JPA로 처리하기 위해 RoomDTO를 Room객체, RoomImage로 변환합니다.
     * 한번에 두가지 종류의 객체를 반환하므로 Map타입을 이용해 반환합니다.
     */
    default Map<String, Object> dtoToEntityWithRoomImage(RoomDTO roomDTO){
        Member member = Member.builder().mno(roomDTO.getWriterMno()).build();
        Map<String, Object> entityMap = new HashMap<>();

        Room room = Room.builder()
                .rno(roomDTO.getRno())
                .title(roomDTO.getTitle())
                .content(roomDTO.getContent())
                .writer(member)
                .build();

        entityMap.put("room", room);
        List<RoomImageDTO> roomImageDTOList = roomDTO.getRoomImageDTOList();

        //RoomImageDTO 처리
        if(roomImageDTOList != null && roomImageDTOList.size() > 0){
            List<RoomImage> roomImageList = roomImageDTOList.stream().map(roomImageDTO -> {
                RoomImage roomImage = RoomImage.builder()
                        .path(roomImageDTO.getPath())
                        .imgName(roomImageDTO.getImgName())
                        .uuid(roomImageDTO.getUuid())
                        .room(room)
                        .build();
                return roomImage;
            }).collect(Collectors.toList());

            entityMap.put("roomImgList", roomImageList);
        }
        return entityMap;

    }

    /**
     * Description :
     * Object[]을 DTO로 변환
     * JPQL의 결과로 나오는 Object[]을 DTO 타입으로 변환하는 기능
     * 이 기능은 Java.util.Function을 이용해서 작성.
     * 현재 예제의 경우 JPQL의 실행결과로 나오는 Object[]을 RoomDTO로 변환.
     * Object[]의 내용은 Room, Member, 댓글의 수는 Long타입으로 나오게 되므로 이를 파라미터로
     * 전달받아서 RoomDTO를 구성하도록 작성함.
     */
    default RoomDTO entityToDto(Room room, Member member, Long replyCount){

        RoomDTO dto = RoomDTO.builder()
                .rno(room.getRno())
                .title(room.getTitle())
                .content(room.getContent())
                .regDate(room.getRegDate())
                .modDate(room.getModDate())
                .writerMno(member.getMno())
                .writerEmail(member.getEmail())
                .writerName(member.getNickname())
                .replyCount(replyCount.intValue())
                .build();

        return dto;
    }

    /**
     * Description : JPA를 통해 나오는 엔티티 객체들을 roomDTO로 변환시킵니다.
     *
     */
    default RoomDTO entitiesToDTO(Room room, Member member, List<RoomImage> roomImages,  Long replyCount, Double avg, Long reviewCount){
        RoomDTO roomDTO = RoomDTO.builder()
                .rno(room.getRno())
                .title(room.getTitle())
                .content(room.getContent())
                .regDate(room.getRegDate())
                .modDate(room.getModDate())
                .writerMno(member.getMno())
                .writerEmail(member.getEmail())
                .writerName(member.getNickname())
                .replyCount(replyCount.intValue())
                .build();

        List<RoomImageDTO> roomImageDTOList = roomImages.stream().map(roomImage ->{
            return RoomImageDTO.builder().imgName(roomImage.getImgName())
                    .path(roomImage.getPath())
                    .uuid(roomImage.getUuid())
                    .build();
        }).collect(Collectors.toList());

        roomDTO.setRoomImageDTOList(roomImageDTOList);
        roomDTO.setAvg(avg);
        roomDTO.setReviewCount(reviewCount.intValue());

        return roomDTO;
    }




}
