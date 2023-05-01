package com.room.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ReplyDTO {
    private Long replyno;
    private String text;
    private String replyer;
    private Long rno;
    private LocalDateTime regDate, modDate;
}
