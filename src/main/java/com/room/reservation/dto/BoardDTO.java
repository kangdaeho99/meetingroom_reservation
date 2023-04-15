package com.room.reservation.dto;


import lombok.*;

import java.time.LocalDateTime;

/**
 *
 * Description :
 * BoardDTO가 BoardEntity와 다른점은 Member를 참조하는 대신에
 * 화면에서 필요한 작성자의 이메일(writerEmail)과 작성자의 이름(writerName)으로 처리하고 있는점입니다.
 * 목록화면에서도 BoardDTO를 이용하기 때문에 댓글의 개수를 의미하는 replyCount도 추가합니다.
 *
 */
@Data
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {
    private Long bno;
    private String title;
    private String content;
    private String writerEmail;
    private String writerName;
    private LocalDateTime regDate;

    private LocalDateTime modDate;
    private int replyCount;
}
