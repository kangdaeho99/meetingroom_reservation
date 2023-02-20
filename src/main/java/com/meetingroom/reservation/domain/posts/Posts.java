package com.meetingroom.reservation.domain.posts;

import com.meetingroom.reservation.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Posts extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    private String author;

    @Builder
    public Posts(String title, String content, String author){
        this.title = title;
        this.content = content;
        this.author = author;
    }

    public void update(String title, String content){
        this.title = title;
        this.content = content;
    }

}


/*
어노테이션 순서를 주요 어노테이션을 클래스에 가깝게 둡니다.
어노테이션 정렬기준은 다음과 같습니다.
@Entity는 JPA의 어노테이션이며, @Getter와 @NoArgsConstructor는 롬복의 어노테이션입니다.
롬복은 코드를 단순화시켜주지만 필수 어노테이션은 아닙니다. 그러다보니 주요 어노테이션인 @Entity를 클래스에 가깝게 두고, 롬복 어노테이션을 그 위로 둡니다.
이렇게 하면 이후에 코틀린 등의 새언어 전환으로 롬복이 더 이상 필요없을 경우 쉽게 삭제할 수 있습니다.
여기서 Posts클래스는 실제 DB의 테이블과 매칭될 클래스이며 보통 Entity 클래스라고도 합니다.
JPA를 사용하시면 DB 데이터에 작업할 경우 실제 쿼리를 날리기보다는, 이 Entity 클래스의 수정을 통해 작업합니다.
Posts 클래스에는 JPA 에서 제공하는 어노테이션들이 몇개 있습니다.
(1) @Entity
- 테이블과 링크될 클래스임을 나타냅니다.
- 기본값으로 클래스의 카멜케이스 이름을 언더스코어 네이밍(_)으로 테이블 이름을 매칭합니다.
ex) SalesManager.java -> sales_manager table
(2) @Id
- 해당 테이블의 PK 필드를 나타냅니다.
(3) @GeneratedValue
- PK의 생성규칙을 나타냅니다.
- 스프링 부트 2.0 에서는 GenerateType.IDENTITY 옵션을 추가해야만 auto_increment가 됩니다.
- 스프링 부트 2.0 버전과 1.5 버전의 차이는 https://jojoldu.tistory.com/295 에 정리됬으니 확인합니다.
(4) @Column
- 테이블의 칼럼을 나타내며 굳이 선언하지 않더라도 해당 클래스의 필드는 모두 칼럼이 됩니다.
- 사용하는 이유는, 기본값 외에 추가로 변경이 필요한 옵션이 있으면 사용합니다.
- 문자열의 경우 VARCHAR(255)가 기본값인데, 사이즈를 500으로 늘리고 싶거나 (ex:title), 타입을 TEXT로 변경하고 싶거나 (ex:content) 등의 경우에 사용됩니다.

웬만하면 Entity의 PK는 Long 타입의 Auto_Increment를 추천합니다. (MySQL 기준으로 이렇게하면 bigInt 타입이 됩니다.)
주민등록번호와 같이 비즈니스상 유니크 키나, 여러 키를 조합한 복합키로 PK를 잡을 경우 난감한 상황이 종종 발생합니다.
(1) FK를 맺을때 다른 테이블에서 복합키 전부를 갖고 있거나, 중간 테이블을 하나 더 둬야하는 상황이 발생합니다.
(2) 인덱스에 좋은 영향을 끼치지 못합니다.
(3) 유니크한 조건이 변경될 경우 PK 전체를 수정해야하는 일이 발생합니다.
주민등록번호, 복합키 등은 유니크 키로 별도로 추가하시는 것을 추천드립니다.

Lombok 어노테이션 추가소개입니다.
(1) @NoArgsConstructor
- 기본 생성자 자동 추가
- public Posts() {} 와 같은 효과

(2) @Getter
- 클래스 내 모든 필드의 Getter 메소드를 자동생성

(3) @Builder
- 해당 클래스의 빌더 패턴 클래스를 생성
- 생성자 상단에 선언 시 생성자에 포함된 필드만 빌더에 포함합니다.

서비스 초기 구축 단계에선 테이블 설계(여기선 Entity 설계)가 빈번하게 변경되는데, 이때 롬복의 어노테이션들은 코드 변경량을 최소화시켜
주기 때문에 적극적으로 사용합니다.

이 Posts 클래스에는 한가지 특이점이 있습니다. 바로 Setter 메소드가 없다는 점입니다.
자바빈 규약을 생각하며 getter/setter를 무작정 생성하는 경우가 있습니다.
이렇게 되면 해당 클래스의 인스턴스 값들이 언제 어디서 변해야하는지 코드상으로 명확하게 구분할 수 없어, 차후 기능 변경시 정말 복잡해집니다.
그래서 Entity 클래스에서는 절대 Setter 메소드를 만들지 않습니다.
대신, 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼 수 있는 메소드를 추가합니다.

예를들어 주문 취소 메소드를 만든다고 가정하면 다음 코드로 비교해봅니다.
[잘못된 사용예]
public class Order{
    public void setStatus(boolean status){
        this.status = status;
    }
}

public void 주문서비스의_취소이벤트(){
    order.setStatus(false);
}

[올바른 사용예]
public class Order{
    public void cancelOrder(){
        this.status = false;
    }
}
public void 주문서비스의_취소이벤트(){
    order.cancelOrder();
}

그러면 여기서 한가지 의문이 남습니다. Setter가 없는 이 상황에서 어떻게 값을 채워 DB에 삽입(insert)해야할까요?
기본적인 구조는 생성자를 통해 최종값을 채운 후 DB에 삽입(insert)하는 것이며, 값 변경이 필요한 경우
해당 이벤트에 맞는 public 메소드를 호출하여 변경하는 것을 전제로합니다.

이 책에서는 생성자 대신에 @Builder를 통해 제공되는 빌더 클래스를 사용합니다.
생성자나 빌더나 생성시점에 값을 채워주는 역할은 똑같습니다. 다만, 생성자의 경우 지금 채워야할 필드가 무엇인지 명확히 지정할 수가 없습니다.
예를들어 다음과 같은 생성자가 있다면 개발자 new Example(b, a) 처럼 a와 b의 위치를 변경해도 코드를 실행하기 전까지는 문제를 찾을 수가 없습니다.
new Example(b,a) -> 파라미터 a와 b의 위치가 바뀌어도 모릅니다.
public Example(String a, String b){
    this.a = a;
    this.b = b;
}
하지만 빌더를 사용하게 되면 다음과 같이 어느 필드에 어떤 값을 채워야할지 명확하게 인지할 수 있습니다.
Example.builder()
    .a(a)
    .b(b)
    .build();

앞으로 모든 예제는 이렇게 빌더 패턴을 적극적으로 사용하니, 잘 익혀두면 좋습니다.
Posts 클래스 생성이 끝났다면, Posts 클래스로 DataBase를 접근하게 해줄 JpaRepository를 생성합니다.


 */