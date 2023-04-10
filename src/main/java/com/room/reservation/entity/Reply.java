package com.room.reservation.entity;


import jakarta.persistence.*;
import lombok.*;


/**
 * Description :
 * Reply 클래스는 회원이 아닌 사람도 댓글을 남길 수 있다고 가정하고 Board와 연관관계를 맺지 않은 상태로 처리합니다.
 *
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "board")  //@ToString 은 항상 exclude
public class Reply extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String text;

    private String replyer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board; //연관관계 지정

}
