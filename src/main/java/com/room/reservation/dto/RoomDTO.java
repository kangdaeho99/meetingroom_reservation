package com.room.reservation.dto;


import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RoomDTO {
    private Long rno;

    private String title;

    private String content;

//  이미지들도 같이 수집해서 전달해야하므로 내부적으로 리스트를 이용해서 수집
    @Builder.Default
    private List<RoomImageDTO> roomImageDTOList = new ArrayList<>();

    private Long writerMno;
    private String writerEmail;

    private String writerName;

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private int replyCount;

    private double avg;

    private int reviewCount;


}
