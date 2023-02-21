package com.meetingroom.reservation.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IndexControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void 메인페이지_로딩(){
        //when
        String body = this.restTemplate.getForObject("/", String.class);

        //then
        assertThat(body).contains("스프링 부트로 시작하는 웹서비스");
    }
}

/*
이번 테스트는 실제로 URL 호출시 페이지의 내용이 제대로 호출되는지에 대한 테스트입니다.
HTML도 결국은 규칙이 있는 문자열입니다.
TestRestTemplate를 통해 "/"로 호출했을때 index.mustache에 포함된 코드들이 있는지 확인하면 됩니다.
전체 코드를 다 검증할 필요는 없으니,
assertThat(body).contains("스프링 부트로 시작하는 웹서비스"); 을 통해"스프링 부트로 시작하는 웹 서비스" 문자열이 포함되어 있는지만 비교합니다.
만약 test failed라면 해당 문자열이 없는 것이고 테스트가 통과한다면 해당 문자열이 있는 것입니다.
테스트 코드를 수행해보면, 정상적으로 코드가 수행되는것을 확인할 수 있습니다.

테스트 코드로 검증했지만, 그래도 이대로 넘어가기 아쉽습니다.
실제로 화면이 잘 나오는지 확인해보겠습니다.
Application.java의 main 메소드를 실행하고 브라우저에서 http://localhost:8080 으로 접속해보겠습니다.
정상적으로 화면이 노출되는것이 확인됩니다. 기본적인 화면생성이 완성되었으니, 좀 더 다양한 주제로 가겠습니다.

 */