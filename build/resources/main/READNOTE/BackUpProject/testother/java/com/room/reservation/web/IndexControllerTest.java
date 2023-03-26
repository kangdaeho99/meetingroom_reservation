package com.room.reservation.web;

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
//        assertThat(body).contains("스프링 부트로 시작하는 웹서비스");
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

---

4.3 게시글 등록화면 만들기
이번에는 게시글 등록화면을 구현해보겠습니다.
앞서 3장에서 PostsApiController로 API를 구현하였으니 여기선 바로 화면을 개발합니다.
아무래도 그냥 HTML만 사용하기에는 멋이 없습니다.
그래서 오픈소스인 부트스트랩을 이용하여 화면을 만들어봅니다.
부트스트랩, 제이쿼리 등 프론트엔드 라이브러리를 사용할 수 있는 방법은 크게 2가지가 있습니다.
하나는 외부 CDN을 사용하는 것이고, 다른 하나는 직접 라이브러리를 받아서 사용하는 방법입니다.
여기서는 전자인 외부 CDN을 사용합니다.
본인의 프로젝트에서 직접 내려받아 사용할 필요도 없고, 사용방법도 HTML/JSP/Mustache에 코드만 한줄 추가하면 되니 굉장히 간단합니다.
실제 서비스에서는 이 방법을 잘 사용하지 않습니다. 결국은 외부 서비스에 우리 서비스가 의존하게 돼버려서, CDN을 서비스하는 곳에 문제가 생기면
덩달아 같이 문제가 생기기 때문입니다.
2개의 라이브러리 부트스트랩과 제이쿼리를 index.mustache에 추가해야합니다. 하지만, 여기서는 바로 추가하지 않고 레이아웃 방식으로 추가해보겠습니다.
레이아웃 방식이란 공통영역을 별도의 파일로 분리하여 필요한 곳에서 가져다 쓰는 방식을 이야기합니다.
이번에 추가할 라이브러리들인 부트스트랩과 제이쿼리는 머스테치 화면 어디서나 필요합니다. 매번 해당 라이브러리를 머스테치 파일에 추가하는 것은
귀찮은 일인, 레이아웃 파일들을 만들어 추가합니다.
[src/main/resources/templates] 디렉토리에 layout 디렉토리를 추가로 생성합니다. 그리고 footer.mustache, header.mustache 파일을
생성합니다.
레이아웃 파일들에 각각 공통 코드를 추가합니다.
화면영역은 코드가 굉장히 많으니 직접 타이핑 하지말고 깃허브의 코드를 가져다 쓰세요.
깃허브 주소 - https://github.com/jojoldu/freelec-springboot2-webservice

[src/main/resources/templates/header.mustache]
[src/main/resources/templates/footer.mustache]
코드를 보면 css와 js의 위치가 서로 다릅니다.
페이지 로딩속도를 높이기 위해 css는 header에, js는 footer에 놓았습니다.
HTML은 위에서부터 코드가 실행되기 때문에 head가 다 실행되고서야 body가 실행됩니다.
즉, head가 다 불러지지 않으면 사용자 족에선 백지 화면만 노출됩니다.
특히 js의 용량이 크면 클수록 body 부분의 실행이 늦어지기 때문에 js는 body 하단에 두어 화면이 다 그려진 뒤에 호출하는것이 좋습니다.
반면 css는 화면을 그리는 역할이므로 head에서 불러오는 것이 좋습니다.
그렇지 않으면 css가 적용되지 않은 깨진 화면을 사용자가 볼 수 있기 때문입니다.
추가로, bootstrap.js의 경우 제이쿼리가 꼭 있어야만 하기 때문에 부트스트랩보다 먼저 호출되도록 코드를 작성했습니다.
보통 앞선 상황을 bootstrap.js가 제이쿼리에 의존한다고 합니다.
라이브러리를 비롯해 기타 HTML 태그들이 모두 레이아웃에 추가되니 이제 index.mustache에는 필요한 코드만 남게됩니다.
index.mustache의 코드는 다음과 같이 변경됩니다.

{{>}}는 현재 머스테치 파일(index.mustache)을 기준으로 다른 파일을 가져옵니다.
{{>layout/header}}
<h1>스프링 부트로 시작하는 웹서비스</h1>
{{>layout/footer}}

레이아웃으로 파일을 분리했으니 index.mustache에 글 등록버튼을 하나 추가해봅니다.

 */