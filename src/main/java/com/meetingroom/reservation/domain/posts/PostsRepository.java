package com.meetingroom.reservation.domain.posts;

import org.springframework.data.jpa.repository.JpaRepository;

//Posts 클래스 생성이 끝났다면, Posts 클래스로 DataBase를 접근하게 해줄 JPARepository를 생성합니다.
public interface PostsRepository extends JpaRepository<Posts, Long> {

}



/*
보통 ibatis나 MyBatis 등에서 Dao라고 불리는 DB Layer 접근자입니다.
JPA에선 Repository 라고 부르며 인터페이스로 생성합니다.
단순히 인터페이스를 생성 후, JpaRepository<Entity 클래스, Pk타입>를 상속하면 기본적인 CRUD 메소드가 자동으로 생성됩니다.
@Repository를 추가할 필요도 없습니다. 여기서 주의하실점은 Entity 클래스와 기본 Entity Repository는 함께 위치해야하는 점입니다.
둘은 아주 밀접한 관계이고, Entity 클래스는 기본 Repository 없이는 제대로 역할을 할 수가 없습니다.
나중에 프로젝트 규모가 커져서 도메인별로 프로젝트를 분리해야한다면 이때 Entity 클래스와 기본 Repository는 함께 움직여야하므로 도메인 패키지에서 함께 관리합니다.
모두 작성되었다면 간단하게 테스트 코드로 기능을 검증해보겠습니다.


 */