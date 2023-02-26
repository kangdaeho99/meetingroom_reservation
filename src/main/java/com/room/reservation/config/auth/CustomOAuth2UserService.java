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
        }123
    },

    ...
}

이외에 다른 소셜 로그인(네이버, 카카오 등)을 추가한다면 직접 다 추가해주어야합니다. 이점을 기억해 해당 블로그에서 어떤 방식을 사용하는지 확인후
참고하면 됩니다.
그럼 다음절에서 구글 로그인 연동을 시작하겠습니다.

5.2 구글 서비스 등록
먼저 구글 서비스에 신규서비스여기서 발급된 인증정보(clientId와 clientSecret)를 통해서 로그인 기능과 소셜 서비스 기능을 사용할 수 있으니 무조건 발급받고 시작해야합니다.
구글 클라우드 플랫폼 주소(http://console.cloud.google.com) 로 이동합니다.
그리고 다음 그림과 같이 [프로젝트 선택] 탭을 클릭합니다.
[새 프로젝트] 버튼을 클릭합니다.
등록될 서비스의 이름을 입력합니다. 원하는 이름으로 지으면 됩니다.
필자는 freelec-springboot2-webservice로 지었습니다.
저는 room-reservation-springboot2-webservice 로 지었습니다.


 */
