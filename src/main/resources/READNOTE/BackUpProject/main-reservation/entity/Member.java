package com.room.reservation.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

/**
 * Description : 회원 (Member) 엔티티 클래스
 * Member클래스는 이메일을 사용자의 아이디 대신에 사용합니다.
 * 이메일 주소를 PK로 이용합니다.
 * 데이터베이스 설계에서도 member 테이블은 PK만을 가지고 있고,FK를 사용하지 않으므로 별도의 참조가 필요하지 않습니다.
 *
 *
 *
 */
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    private String email;
    private String password;
    private String name;
}
