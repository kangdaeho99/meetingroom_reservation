package com.room.reservation.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name= "tbl_memo")
@ToString
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Memo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    @Column(length = 200, nullable = false)
    private String memoText;

}


/*
Spring Data JPA가 필요한 2가지.
1. JPA를 통해서 관리하게 되는 객체(이하 엔티티 객체(Entity Object))를 위한 엔티티 클래스
2. 엔티티 객체들을 처리하는 기능을 가진 Repository



 */