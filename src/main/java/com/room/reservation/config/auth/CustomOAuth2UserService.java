package com.room.reservation.config.auth;


public class CustomOAuth2UserService {
}


/*
스프링 시큐리티는 막강한 인증(Authentication)과 인가(Authorization) (혹은 권한 부여) 기능을 가진 프레임워크입니다.
사실상 스프링 기반의 애플리케이션에서는 보안을 위한 표준이라고 보면 됩니다.
인터셉터, 필터 기반의 보안 기능을 구현하는 것보다 스프링 시큐리티를 통해 구현하는 것을 적극적으로 권장하고 있습니다.
스프링의 대부분 프로젝트들(Mvc, Data, Batch 등등) 처럼 확장성을 고려한 프레임워크다 보니 다양한 요구사항을 손쉽게 추가하고 변경할 수 있습니다.
이런 손쉬운 설정은 특히나 스프링 부트 1.5 에서 2.0 으로 넘어오면서 더욱 더 강력해졌습니다.
이번 장에서는 스프링 시큐리티와 OAuth 2.0을 구현한 구글 로그인을 연동하여 로그인 기능을 만들어보겠습니다.

5.1 스프링 시큐리티와 스프링 시키류티 Oauth2 클라이언트
많은 서비스에서 로그인 기능을 id/password 방식보다는 구글, 페이스북, 네이버 로그인과 같은 소셜 로그인기능을 사용합니다.
왜 많은 서비스에서 소셜 로그인을 사용할까요?
필자가 생각하는 이유는 직접 구현할 경우 배보다 배꼽이 커지는 경우가 많기 때문입니다.
직접 구현하면 당므을 전부 구현해야합니다. OAuth를 써도 구현해야하는 것은 제외했습니다.
-로그인시보안
-회원가입시 이메일 혹은 전화번호 인증
-비밀번호 찾기
-비밀번호 변경
-회원정보 변경

OAuth 로그인 구현시 앞선 목록의 것들을 모두 구글, 페이스북, 네이버 등에 맡기면 되니 서비스 개발에 집중할 수 있습니다.

--스프링 부트 1.5 vs 스프링 부트 2.0
스프링 부트 1.5에서의 OAuth2 연동방법이 2.0 에서는 크게 변경되었습니다.
하지만 인터넷 자료들(블로그나 깃허브 등)을 보면 설정방법에 크게 차이가 없는 경우를 자주 봅니다.
이는 spring-security-oauth2-autoconfigure 라이브러리 덕분입니다.
spring-security-oauth2-autoconfigure

spring-security-oauth2-autoconfigure 라이브러리를 사용할 경우 스프링 부트 2에서도 1.5에서 쓰던 설정을 그대로 사용할 수 있습니다.
새로운 방법을 쓰기 보다는 기존에 안전하게 작동하던 코드를 사용하는것이 아무래도 더 확실하므로 많은 개발자가 이 방식을 사용해왔습니다.
하지만 이책에서는 스프링 부트 2 방식인 Spring Security Oauth2 Client 라이브러리를 사용해서 진행합니다.
이유는 다음과 같습니다.
-스프링 팀에서 기존 1.5에서 사용되던 spring-security-oauth 프로젝트는 유지상태(maintenance mode)로 결정했으며,
더는 신규기능은 추가하지 않고 버그 수정 정도의 기능만 추가될 예정, 신규 기능은 새 oauth2 라이브러리에서만 지원하겠다고 선언했습니다.
-스프링 부트용 라이브러리(starter) 출시
-기존에 사용하던 방식은 확장 포인트가 적절하게 오픈되어있지 않아 직접 상속하거나 오버라이딩 해야하고 신규 라이브러리의 경우 확장 포인트를 고려해서
설계된 상태입니다.

그리고 한가지 더 이야기하자면, 이 책 이외에 스프링 부트 2 방식의 자료를 찾고 싶은 경우, 인터넷 자료들 사이에서 다음 두 가지만 확인하면 됩니다.
먼저 spring-security-oauth2-autoconfigure 라이브러리를 썻는지를 확인하고 application.properties 혹은 application.yml 정보가
다음과 같이 차이가 있는지 비교해야합니다.

Spring Boot 1.5와 2.0 의 설정차이
[Spring Boot 1.5 Spring-security configure]
google :
    client :
        clientId : 인증정보
        clientSecret : 인증정보
        accessTokenUri : https://accounts.google.com/o/oauth2/token
        userAuthorizationUri : https://accounts.google.com/o/oauth2/auth
        clientAuthenticationScheme : form
        scope : email, profile
    resource :
        userInfoUri : https://www.googleapis.com/oauth2/v2/userinfo

[Spring Boot 2.0 Spring-security configure
spring:
    security:
        oauth2:
            client:
                clientId:인증정보
                clientSecret:인증정보

스프링부트 1.5 방식에서는 URL 주소를 모두 명시해야하지만, 2.0 방식에서는 client 인증정보만 입력하면 됩니다.
1.5 버전에서 직접 입력했던 값들은 2.0 버전으로 오면서 모두 enum으로 대체되었습니다.
CommonOAuth2Provider라는 enum이 새롭게 추가되어 구글, 깃허브, 페이스북, 옥타(Okta)의 기본 설정값은 모두 여기서 제공합니다.

public enum CommonOAuth2Provider{
    GOOGLE{
        @Override
        public Builder getBuilder(String registrationId){
            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
            builder.scope("openid", "profile", "email");
            builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
            builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
            builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
            builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
            builder.userNameAttributeName(IdTokenClaimNames.SUB);
            builder.clientName("Google");
            return builder;
        }
    },

    ...
}

이외에 다른 소셜 로그인(네이버, 카카오 등)을 추가한다면 직접 다 추가해주어야합니다. 이점을 기억해 해당 블로그에서 어떤 방식을 사용하는지 확인후
참고하면 됩니다.
그럼 다음절에서 구글 로그인 연동을 시작하겠습니다.

5.2 구글 서비스 등록
먼저 구글 서비스에 신규서비스를 생성합니다.
여기서 발급된 인증정보(clientId와 clientSecret)를 통해서 로그인 기능과 소셜 서비스 기능을 사용할 수 있으니 무조건 발급받고 시작해야합니다.
구글 클라우드 플랫폼 주소(http://console.cloud.google.com) 로 이동합니다.
그리고 다음 그림과 같이 [프로젝트 선택] 탭을 클릭합니다.
[새 프로젝트] 버튼을 클릭합니다.
등록될 서비스의 이름을 입력합니다. 원하는 이름으로 지으면 됩니다.
필자는 freelec-springboot2-webservice로 지었습니다.
저는 room-reservation-springboot2 로 지었습니다.
생성이 완료된 프로젝트를 선택하고 왼쪽 메뉴 탭을 클릭해서 API 및 서비스 카테고리로 이동합니다.
그 이후 사이드바 중간에 있는 [사용자 인증정보]를 클릭하고 [사용자 인증정보 만들기] 버튼을 클릭합니다.
사용자 인증정보에는 여러 메뉴가 있는데 이 중 이번에 구현할 소셜 로그인은 OAuth 클라이언트 ID로 구현합니다.
[OAuth 클라이언트 ID] 항목을 클릭합니다.
바로 클라이언트 ID가 생성되기 전에 동의 화면 구성이 필요하므로 안내에 따라 [동의화면구성] 버튼을 클릭합니다.
동의 화면에서는 3개의 탭이 있는데, 이중 'OAuth 동의 화면' 탭에서 다음과 같이 각 항목을 작성합니다.
처음에 시작하면 책에는 안나오지만 사용자 유형에 user Type에 [내부] [외부] 가 나오는데
모든 사용자가 사용하게 하도록 [외부]를 선택합니다. 내부는 구글 워크스페이스 사용자만 사용가능합니다.
다음으로 넘어가서 [OAuth 동의 화면]입니다.
-애플리케이션 이름 : 구글 로그인 시 사용자에게 노출될 애플리케이션 이름을 이야기합니다.
-지원이메일 : 사용자 동의화면에서 노출될 이메일 주소입니다. 보통은 서비스의 help 이메일 주소를 사용하지만, 여기서는 독자의 이메일 주소를 사용하시면 됩니다.
-Google API의 범위 : 이번에 등록할 구글 서비스에서 사용할 범위 목록입니다. 기본값은 email/profile/openid 이며, 여기서는 딱 기본 범위만 사용합니다.
                    이외 다른 정보들도 사용하고 싶다면 범위 추가 버튼으로 추가하면 됩니다.


해당 화면에서 애플리케이션 이름 : room-reservation-webservice
지원이메일 : 내 이메일
Google API의 범위 : email, profile, openid
동의 화면 구성이 끝났으면 화면 제일 아래에 저장 버튼을 클릭하고 다음 그림과 같이 OAuth 클라이언트 ID만들기 화면으로 바로 이동합니다.
프로젝트 이름을 등록합니다.
테스트 계정은 필요없으므로 넘어갑니다.
완료했다면, [사용자 인증정보] -> [사용자 인증정보 만들기] 를 클릭합니다.
사용자 인증정보에는 여러 메뉴가 있는데 이 중 이번에 구현할 소셜 로그인은 OAuth 클라이언트 ID로 구현합니다.
[OAuth 클라이언트 ID] 항목을 클릭합니다.
[애플리케이션 유형] 을 웹 애플리케이션으로 선택합니다.
[승인된 리디렉션 URI] 에 http://localhost:8080/login/oauth2/code/google 를 넣습니다.

승인된 리디렉션 URL
- 서비스에서 파라미터로 인증정보를 주었을때 인증이 성공하면 구글에서 리다이렉트할 URL입니다.
- 스프링 부트 2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드} 로 리다이렉트 URL을 지원하고 있습니다.
- 사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들필요가 없습니다. 시큐리티에서 이미 구현해놓은 상태입니다.
- 현재는 개발단계이므로 http://localhost:8080/login/oauth2/code/google 로만 등록합니다.
- AWS 서버에 배포하게 되면 localhost 외에 추가로 주소를 추가해야하며, 이건 이후 단계에서 진행하겠습니다.

생성버튼으르 클릭하면 다음 그림과 같이 생성된 클라이언트 정보를 볼 수 있고 생성된 애플리케이션을 클릭하면 그림과 같이 인증정보를 볼 수 있습니다.
완성되면 클라이언트 ID와 클라이언트 보안비밀의 암호가 나오는데
클라이언트 ID(Client ID)와 클라이언트 보안비밀(Client Secret) 코드를 프로젝트에서 설정하겠습니다.

- application-oauth 등록
4장에서 만들었던 application.properties 가 있는 src/main/resources/ 디렉토리에 application-oauth.properties 파일을 생성합니다.
[\room_reservation\src\main\resources\application-oauth.properties] 를 생성합니다.
그리고 해당 파일에 클라이언트 ID와 클라이언트 보안 비밀코드를 다음과 같이 등록합니다.
spring.security.oauth2.client.registration.google.client-id = 클라이언트 ID
spring.security.oauth2.client.registration.google.client-secret = 클라이언트 보안비밀
spring.security.oauth2.client.registration.google.scope = profile, email

-많은 예제에서는 scope = profile, email 을 별도로 지정하고 잇지 않습니다.
- 기본값이 openid, profile, email 이기 때문입니다.
- 강제로 profile, email 을 등록한 이유는 openid 라는 scope가 있으면 Open Id Provider로 인식하기 때문입니다.
- 이렇게 되면 OpenId Provider인 서비스(구글)와 그렇지 않은 서비스(네이버/카카오 등)로 나눠서 각각 OAuth2Service를 만들어야합니다.
- 하나의 OAuth2Service로 사용하기 위해 일부러 openid scope를 빼고 등록합니다.

스프링 부트에서는 properties의 이름을 application-xxx.properties로 만들면 xxx 라는 이름의
profile이 생성되어 이를 통해 관리할 수 있습니다.
즉, profile=xxx 라는 식으로 호출하면 해당 properties의 설정들을 가져 올 수 있습니다.
호출하는 방식은 여러 방식이 있지만 이책에서는 스프링 부트의 기본설정파일인 application.properties에서
application-oauth.properties를 포함하도록 구성합니다.

[\src\main\resources\application.properties]에 다음과 같이 코드를 추가합니다.
"spring.profiles.include = oauth"
이제 위의 설정을 통해 application-oauth.properties 설정파일을 사용할 수 있게 되었습니다.

--- .gitignore 에 application-oauth.properties 등록
구글 로그인을 위한 클라이언트 ID와 클라이언트 보안 비밀은 보안이 중요한 정보들입니다.
이들이 외부에 노출될 경우 언제든 개인정보를 가져갈 수 있는 취약점이 될 수 있습니다.
이 책으로 진행 중인 독자는 깃허브와 연동하여 사용하다보니 application-oauth.properties
파일이 깃허브에 올라갈 수 있습니다.
보안을 위해 깃허브에 application-oauth.properties 파일이 올라가는 것을 방지하겠습니다.
1장에서 만들었던 .gitignore에 다음과 같이 하나 줄의 코드를 추가합니다.

application-oauth.properties

추가한뒤 커밋했을때 커밋파일 목록에  application-oauth.properties가 나오지 않으면
성공입니다.
만약 .gitignore에 추가했음에도 여전히 커밋 목록에 노출된다면 이는 Git의
캐시 문제 떄문입니다. 이에대한 해결책은 필자의 블로그를 참고하시길 바랍니다.
gitignore가 작동하지 않을때 대처법(https://jojoldu.tistory.com/307)












 */
