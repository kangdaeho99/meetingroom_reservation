package com.room.reservation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@EnableJpaAuditing : JPA Auditing을 활성화시킵니다.
//@EnableJpaAuditing 를 삭제시킵니다. 이유는 @EnableJpaAuditing을 사용하기 위해서는 최소 하나의 @entity 클래스가 필요한데,
//테스트 단에서는 @WebMvcTest이므로 당연히 없습니다. @EnableJpaAuditing가 SpringBootApplication과 함께 있다보니
//@WebMvcTest에서도 스캔하게 도ㅓㅣ었습니다. 그래서 @EnableJpaAuditing과 @SpringBootApplication을 둘을 분리합니다.
//@SpringBootApplication : 스프링부트의 자동설정, 스프링 Bean 읽기와 생성
//SpringBootApplication이 있는 위치부터 설정을 읽어가기 때문에 이 클래스는 항상 프로젝트의 최상단에 위치해야합니다.
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
//        Main 메소드의 SpringApplication.run으로 내장 WAS를 실행
//        톰캣없이 가능. 스프링부트로 만들어진 Jar파일(실행가능한 Java 패키징 파일)로 실행하면 됩니다.
//        내장 WAS를 통해 언제 어디서나 같은 환경에서 스프링 부트를 배포할수 있습니다.
//        외장 WAS(ex. 톰캣)을 사용하면 모든 서버는 WAS의 종류, 버전, 설정을 일치시키고,
//        새로운 서버가 추가되면 모든 서버가 같은 WAS 환경을 구축해야만합니다.
        SpringApplication.run(Application.class, args);
    }
}

