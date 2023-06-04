//package com.room.reservation.web;
//
//import com.room.reservation.config.auth.SecurityConfig;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.FilterType;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.hamcrest.Matchers.is;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//
////@RunWith : 테스트를 진행할때 JUnit에 내장된 실행자 외에 다른 실행자를 실행시킵니다.
////여기서는 SpringRunner라는 스프링 실행자를 사용합니다.
////즉, 스프링부트 테스트와 JUnit 사이에 연결자 역할을 합니다.
//@RunWith(SpringRunner.class)
////@WebMvcTest(controllers = HelloController.class)
////여러 스프링 테스트 어노테이션 중, Web(Spring MVC)에 집중할 수 있는 어노테이션입니다.
////선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있습니다.
////단, @Service, @Component, @Repository 등은 사용할 수 없습니다.
////여기서는 컨트롤러만 사용하기 때문에 선언합니다.
////
////SpringSecurity 와 연관하여 테스트하려고 하는데, 여기서 위에 설명이 있듯
//// 선언할 경우 @Controller, @ControllerAdvice 등을 사용할 수 있습니다.
//// 그러니 SecurityConfig는 읽었지만, SecurityConfig를 생성하기 위해 필요한 CustomOAuth2UserService는 읽을 수가 없어 앞에서와
////같이 에러가 발생한 것입니다. 그래서 이 문제를 해결하기 위해 다음과 같이 스캔대상에서 SecurityConfig를 제거합니다.
//@WebMvcTest(controllers = HelloController.class,
//    excludeFilters = {
//        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes= SecurityConfig.class)
//    }
//)
//public class HelloControllerTests {
//
////    @Autowired : 스프링이 사용하는 빈(Bean)을 주입받습니다.
////    private MockMVC : 웹 API를 테스트할때 사용, 스프링 MVC 테스트의 시작, 이 클래스를 통해 HTTP GET, POST 등에 대한
////    API 테스트를 할 수 있습니다.
//    @Autowired
//    private MockMvc mvc;
//
//
//    @WithMockUser(roles="USER")
//    @Test
//    public void hello가_리턴되다() throws Exception{
//        String hello = "hello";
//
////        mvc.perform(get("/hello")) : mockMVC를 통해 /hello 주소로 HTTP GET요청을 합니다.
////        체이닝이 지원되어 아래와 같이 여러 검증 기능을 이어서 선언할 수 있습니다.
////
////        andExpect(status().isOk()) : mvc.perform의 결과를 검증합니다.
////        HTTP Header의 Status를 검증합니다.
////        우리가 흔히 알고있는 200,404,500 등의 상태를 검증합니다.
////        여기선 OK 즉, 200인지 아닌지를 검증합니다.
////        andExpect(content().string(hello)) :
////        mvc.perform의 결과를 검증합니다.
////        Controller에서 "hello"를 리턴하기 때문에 이 값이 맞는지 검증합니다.
//        mvc.perform(get("/hello"))
//                .andExpect(status().isOk())
//                .andExpect(content().string(hello));
//    }
//
//
//    @WithMockUser(roles="USER")
//    @Test
//    public void helloDTO가_리턴되다() throws Exception{
//        String name="hello";
//        int amount = 1000;
//
////        param : API 테스트할때 사용될 요청 파라미터를 설정합니다.
////        단, 값은 string만 허용합니다.
////        그래서 숫자/날짜 등의 데이터도 등록할때는 문자열로 변경하여 가능합니다.
////        JsonPath : JSON 응답값을 필드별로 검증할 수 있는 메소드입니다.
////        $ 를 기준으로 필드명을 명시합니다.
////        여기서는 name과 amount를 검증하니 $.name, $.amount로 검증합니다.
////        -> 결과 : JSON이 리턴되는 API 역시 정상적으로 테스트가 통과하는 것을 확인할 수 있습니다.
////      확실한 라이브러리를 import 해야만 합니다.
////      JSONPath : import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
////      is : import static org.hamcrest.Matchers.is;
////
//        mvc.perform(
//                get("/hello/dto")
//                        .param("name", name)
//                        .param("amount", String.valueOf(amount)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name", is(name)))
//                .andExpect(jsonPath("$.amount", is(amount)));
//    }
//}
