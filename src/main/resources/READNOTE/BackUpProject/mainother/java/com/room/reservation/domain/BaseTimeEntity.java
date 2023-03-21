package com.room.reservation.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter

//@MappedSuperclass : JPA Entity 클래스들이 BaseTimeEntity을 상속할경우 필드들(createdDate, modifiedDate)도 칼럼으로 인식하도록 합니다.
@MappedSuperclass
//@EntityListeners(AuditingEntityListener.class) : BaseTimeEntity 클래스에 Auditing 기능을 포함시킵니다.
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    //@CreatedDate - Entity가 생성되어 저장될때 시간이 자동 저장됩니다.
    @CreatedDate
    private LocalDateTime createdDate;

    //@LastModifiedDate - 조회한 Entity의 값을 변경할때 시간이 자동 저장됩니다.
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}

/*
BaseTimeEntity 클래스는 모든 Entity의 상위 클래스가 되어 Entity들의 createdDate, modifiedDate를 자동으로 관리하는 역할입니다.

그리고 Posts클래스가 BaseTimeEntity를 상속받도록 변경합니다.
public class Posts extends BaseTimeEntity {
마지막으로 JPA Auditing 어노테이션들을 모두 활성화할 수 있도록 Application 클래스에 활성화 어노테이션 하나를 추가하겠습니다.
@EnableJpaAuditing //JPA Auditing 활성화 어노테이션을 추가합니다.

자그러면 실제코드는 완성이 되었습니다. 기능이 잘작동하는지 테스트 코드를 작성해보겠습니다.
JPA Auditing 테스트 코드 작성하기
PostRepositoryTest클래스에 테스트 메소드를 하나 더 추가하겠습니다.



 */
