//package com.room.reservation.web.dto;
//
//
//import org.junit.Test;
//import static org.assertj.core.api.Assertions.assertThat;
//
//public class HelloResponseDtoTest {
//    @Test
//    public void 롬복_기능_테스트(){
//        //given
//        String name = "test";
//        int amount = 1000;
//
//        //when
//        HelloResponseDto dto = new HelloResponseDto(name, amount);
//
//        //then
////        assertThat : assertj라는 테스트 검증 라이브러리의 검증 메소드입니다.
////        검증하고 싶은 대상을 메소드 인자로 받습니다.
////        메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용할 수 있습니다.
//
////        isEqualTo : assertj의 동등 비교메소드입니다.
////        assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을대만 성공입니다.
////        또 여기서 assertThat의 라이브러리는 import static org.assertj.core.api.Assertions.assertThat; 을 사용합니다.
////        JUnit의 기본 assertThat을 처음에 사용햇다가 오류가 났는데, 책에서는
////        assertj의 assertThat을 사용한다고 했다.
////        junit과 비교했을때 장점은
////        1. Core Matchers와 달리 추가적으로 라이브러리가 필요하지 않다.
////        -junit의 assertThat을 사용하게 되면 is(), isEqual()같은 Core Matchers 라이브러리가 필요하다.
////        2. 자동완성이 좀더 확실하다.
////        -IDE에서는 CoreMatchers와 같은 Matcher 라이브러리의 자동완성 지원이 약하다.
//        //variable name not initialized in the default constructor 이란 오류가 나왔었는데
//        //그레이블 버전이 5부터는 implementation('org.projectlombok:lombok:1.18.10') 이렇게 버전을1.18.10 버전을 명시합니다.
//        assertThat(dto.getName()).isEqualTo(name);
//        assertThat(dto.getAmount()).isEqualTo(amount);
//    }
//}
