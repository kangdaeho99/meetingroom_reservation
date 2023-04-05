package com.room.reservation.entity;

import jakarta.persistence.*;
import lombok.*;


/**
 * Description : 게시글 클래스
 * Member의 이메일(PK)을 FK로 참조하는 구조
 * 초기의 설정에서는 우선 연관관계를 작성하지 않고 순수하게 작성
 * 나중에 회원과의 연관관계를 고려해서 작성자에 해당하는 필드는 작성하지 않습니다.
 *
 * Lazy Loading을 활용합니다.
 *
 *
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "writer")  //@ToString 은 항상 exclude포함
public class Board extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    @ManyToOne (fetch = FetchType.LAZY) //명시적으로 Lazy 로딩 지정
    private Member writer; //연관관계 지정
}
