package com.room.reservation.repository;

import com.room.reservation.entity.Memo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoRepository extends JpaRepository<Memo, Long> {

}
/*
MemoRepository는 특이하게도 인터페이스 자체.
JpaRepository 인터페이스를 상속하는 것만으로 모든 작업이 끝남.
JpaRepository를 사용할때는 엔티티의 타입 정보(Memo 클래스 타입)와 @Id의 타입을 지정하게 됨.
Spring Data JPA는 인터페이스 선언만으로 자동으로 스프링 Bean으로 등록됨.
(스프링이 내부적으로 인터페이스 타입에 맞는 객체 생성하여 빈으로 등록함)

 */