package com.room.reservation.config.auth;


import com.room.reservation.config.auth.dto.OAuthAttributes;
import com.room.reservation.config.auth.dto.SessionUser;
import com.room.reservation.domain.user.User;
import com.room.reservation.domain.user.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

//       registrationId : 현재 로그인 진행 중인 서비스를 구분하는 코드입니다.
//       -지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용합니다.
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
//        userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드 값을 이야기합니다. Primary Key와 같은 의미입니다.
//        -구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않습니다. 구글의 기본코드는 'sub'입니다.
//        이후 네이버 로그인과 구글 로그인을 동시 지원할때 사용합니다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

//        OAuthAttributes: OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스입니다.
//        이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용합니다.
//        바로 아래에서 이 클래스의 코드가 나오니 차례로 생성하시면 됩니다.
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        User user = saveOrUpdate(attributes);
//        SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto 클래스입니다.
//        왜 User 클래스를 쓰지 않고 새로 만들어서 쓰는지 뒤이어서 상세하게 설명하겠습니다.
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                attributes.getAttributes(),
                attributes.getNameAttributeKey());
    }

    private User saveOrUpdate(OAuthAttributes attributes){
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
                .orElse(attributes.toEntity());
        return userRepository.save(user);
    }
}





/*
스프링 시큐리티 전체 흐름
---
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

생성이 완료된 프로젝트를 선택하고 왼쪽 메뉴 탭을 클릭해서 API 및 서비스 카테고리로 이동합니다.
사이드바 중간에 있는 [사용자 인증 정보]를 클릭하고 [사용자 인증 정보 만들기] 버튼을 클릭합니다.
사용자 인증정보에는 여러 메뉴가 있는데 이 중 이번에 구현할 소셜 로그인은 OAuth 클라이언트 ID로 구현합니다.
[OAuth 클라이언트 ID] 항목을 클릭합니다.
바로 클라이언트 ID가 생성되기 전에 동의 화면 구성이 필요하므로 안내에 따라 [동의 화면 구성] 버튼을 클릭합니다.
동의 화면에서는 3개의 탭이 있는데, 이 중 'OAuth 동의 화면' 탭에서 다음과 같이 각 항목을 작성합니다.
애플리케이션 이름 : freelec-springboot2-webservice
지원 이메일 : 본인 이메일
Google API 범위 : email, profile, openid
- 애플리케이션 이름 : 구글 로그인 시 사용자에게 노출될 애플리케이션 이름을 이야기합니다.
- 지원 이메일 : 사용자 동의 화면에서 노출될 이메일 주소입니다. 보통은 서비스의 help 이메일 주소를 사용하지만, 여기서는 독자의 이메일 주소를 사용하시면 됩니다.
- Google API 의 범위 : 이번에 등록할 구글 서비스에서 사용할 범위 목록입니다. 기본값은 email/profile/openid 이며, 여기서는 딱
기본범위만 사용중입니다. 이와 다른 정보들도 사용하고 싶다면 범위 추가 버튼으로 추가하면 됩니다.
동의 화면 구성이 끝났으면 화면 제일 아래에 저장 버튼을 클릭하고 다음과 같이 OAuth 클라이언트 ID 만들기 화면으로 바로 이동합니다.
화면 아래로 내려가면 다음과 같이 URL 주소를 등록해야합니다. 여기서 [승인된 리디렉션 URL] 항목만 그림과 같이 값을 등록합니다.
프로젝트 이름:freelec-springboot2-webservice
애플리케이션 유형 : 웹 애플리케이션
승인된 리디렉션 URL : http://localhost:8080/login/oauth2/code/google
승인된 리디렉션 URL
- 서비스에서 파라미터로 인증정보를 주었을때 인증이 성공하면 구글에서 리다이렉트할 URL입니다.
- 스프링부트2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드} 로 리다이렉트 URL을 지원하고 있습니다.
- 사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들 필요가 없습니다.
    시큐리티에서 이미 구현해놓은 상태입니다.
- 현재는 개발 단계이므로 http://localhost:8080/login/oauth2/code/google 로만 등록합니다.
- AWS 서버에 배포하게 되면 localhost 외에 추가로 주소를 추가해야하며, 이건 이후 단계에서 진행하겠습니다.
생성 버튼을 클릭하면 다음 그림과 같이 생성된 클라이언트 정보를 볼 수 있고 생성된 애플리케이션을 클릭하면 그림과 같이 인증정보를 볼 수 있습니다.
클라이언트 ID(clientID)와 클라이언트 보안비밀코드(clientSecret)를 프로젝트에서 설정하겠습니다.

application-oauth를 등록합니다.
[\room_reservation\src\main\resources\application-oauth.properties]
4장에서 만들었던 application.properties 가 있는 src/main/rsources/ 디렉토리에 application-oauth.properties 파일을 생성합니다.
그리고 해당 파일에 클라이언트 ID와 클라이언트 보안비밀코드를 다음과 같이 등록합니다.
spring.security.oauth2.client.registration.google.client-id =
spring.security.oauth2.client.registration.google.client-secret =
spring.security.oauth2.client.registration.google.scope = profile, email

- 많은 예제에서는 이 scope를 별도로 등록하지 않고 있습니다.
- 기본값이 openid, profile, email 이기 때문입니다.
- 강제로 profile, email을 등록한 이유는 openid라는 scope가 있으면 open id provider로 인식하기 때문입니다.
- 이렇게 되면 OpenIdProvider인 서비스(구굴)와 그렇지 않은 서비스(네이버/카카오 등) 로 나눠서 각각 OAuth2Service를 만들어야합니다.
- 하나의 OAuth2Service로 사용하기 위해 일부러 openid scope를 뺴고 등록합니다.

스프링 부트에서는 properties의 이름을 application-xxx.properties로 만들면 xxx라는 이름의 profile이 생성되어 이를 통해 관리할 수 있습니다.
즉, profile=xxx라는 식으로 호출하면 해당 properties의 설정들을 가져올 수 있습니다. 호출하는 방식은 여러 방식이 있지만 이 책에서는
스프링 부트의 기본 설정 파일인 application.properties에서 application-oauth.properties를 포함하도록 구성합니다.
application.properties에 다음과 같이 코드를 추가합니다.
-spring.profiles.include=oauth
이제 이 설정값을 사용할 수 있게 되었습니다.

.gitignore 등록
구글 로그인을 위한 클라이언트 ID와 클라이언트 보안 비밀은 보안이 중요한 정보들입니다.
이들이 외부에 노출될 경우 언제든 개인정보를 가져갈 수 있는 취약점이 될 수 있습니다.
이 책으로 진행중인 독자는 깃허브와 연동하여 사용하다 보니 application-oauth.properties 파일이 깃허브에 올라갈 수 있습니다.
보안을 위해 깃허브에 application-oauth.properties 파일이 올라가는 것을 방지하겠습니다.
1장에서 만들었던
[\room_reservation\.gitignore]에 다음과 같이 한줄의 코드를 추가합니다.
이제 이 설정값을 사용할 수 있게 되었습니다.
추가한 뒤 커밋했을때 커밋파일 목록에 application-oauth.properties가 나오지 않으면 성공입니다.
만약 .gitignore에 추가했음에도 여전히 커밋 목록에 노출된다면 이는 Git의 캐시문제 때문입니다.
이에 대한 해결책은 필자의 블로그를 참고하시길 바랍니다.
.gitignore가 작동하지 않을때 대처법(https://jojoldu.tistory.com/307) 를 확인합니다.(나는 여기서 .gitignore가 작동하면
특유의 색깔이 바뀌기도하고, git에서 커밋전 아예 아무것도 안뜨게되는데 이것을 하지 않아 clientid를 삭제하고 다시 만들었다. 처음에 잘 설정해야합니다.)

5.3 구글 로그인연동
구글의 로그인 인증정보를 발급받았으니 프로젝트 구현을 진행하겠습니다.
먼저 사용자 정보를 담당할 도메인인 User클래스를 생성합니다.
패키지는 domain 아래에 user 패키지를 생성합니다.
[\src\main\java\com\room\reservation\domain_user\User.java]

[\src\main\java\com\room\reservation\domain_user\Role.java]
각 사용자의 권한을 관리할 Enum 클래스 Role을 생성합니다.

[\src\main\java\com\room\reservation\domain_user\UserRepository.java]
마지막으로 User의 CRUD를 책임질 UserRepository도 생성합니다.
UserEntity 관련 코드를 모두 작성했으니 본격적으로 시큐리티 설정을 진행하겠습니다.

---스프링 시큐리티 설정---
먼저 build.gradle에 스프링 시큐리티 관련 의존성 하나를 추가합니다.

//스프링 시큐리티 관련 의존성 추가
implementation('org.springframework.boot:spring-boot-starter-oauth2-client')

build.gradle 설정이 끝났으면 OAuth 라이브러리를 이용한 소셜로그인 설정 코드를 작성합니다.
config.auth 패키지를 생성합니다. 앞으로 시큐리티 관련 클래스는 모두 이곳에 담는다고 보면 됩니다.
[\src\main\java\com\room\reservation\config\auth]

설정 코드 작성이 끝났다면 CustomOAuth2UserService 클래스를 생성합니다.
[\src\main\java\com\room\reservation\config\auth\CustomOAuth2UserService.java]
이 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원합니다.
구글 사용자 정보가 업데이트 되었을 떄를 대비하여 update 기능도 같이 구현되었습니다.
사용자의 이름이나 프로필 사진이 변경되면 User Entity에도 반영됩니다.
CustomOAuth2UserService 클래스까지 생성되었다면 OAuthAttributes 클래스를 생성합니다.
필자의 경우 OAuthAttributes는 Dto로 보기 때문에 config.auth.dtio 패키지를 만들어 해당 패키지에 생성했습니다.
[\src\main\java\com\room\reservation\config\auth\dto\OAuthAttributes.java]

이제는 config.auth.dto 패키지에 SessionUser 클래스를 추가합니다.
[\src\main\java\com\room\reservation\config\auth\dto\SessionUser.java]
SessionUser에는 인증된 사용자 정보만 필요합니다. 그 외에 필요한 정보들은 없으니 name, email, picture만 필드로 선언합니다.
코드가 다 작성되었다면 앞서 궁금했던 내용을 잠시 알아보겠습니다.

왜 User 클래스를 사용하면 안되나요? (CustomOAuth2UserService에서 보면  httpSession.setAttribute("user", new SessionUser(user));
로 SessionUser를 사용한 것을 볼 수 있습니다.
만약 User 클래스를 그대로 사용했다면 다음과 같은 에러가 발생합니다. 다음 내용을 읽기 전에 먼저 생각해보면 좋겠습니다.
Failed to convert from type [java.lang.Object] to type [byte[]] for value 'com.room.reservation.domain.user.user@!#!@#
이는 세션에 저장하기 위해 User 클래스를 세션에 저장하라겨 하니, User 클래스에 직렬화를 구현하지 않았다는 의미의 에러입니다.
그럼 오류를 해결하기 위해 User 클래스에 직렬화 코드를 넣으면 될까요? 그것에 대해서는 생각해볼것이 많습니다.
이유는 User 클래스가 엔티티이기 때문입니다. 엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될지 모릅니다.
예를들어 @OneToMany, @ManyToMany 등 자식 엔티티를 갖고있다면 직렬화 대상에 자식들까지 포함되니 성능 이슈, 부수효과가 발생할 확률이 높습니다.
그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 됩니다.
모든 시큐리티 설정이 끝났습니다. 그럼 로그인 기능을 한번 테스트 해보겠습니다.

로그인테스트
스프링 시큐리티가 잘 적용되었는지 확인하기 위해 화면에 로그인 버튼을 추가해보겠습니다.
index.mustache에 로그인 버튼과 로그인 성공 시 사용자 이름을 보여주는 코드입니다.
[\src\main\resources\templates\index.mustache]

[\src\main\java\com\room\reservation\web\IndexController.java] 그 이후 Index.mustache에서 uerName을 사용할 수 있게
IndexController에서 userName을 model에 저장하는 코드를 작성합니다.

그럼 한번 프로젝트를 실행해서 테스트해 보겠습니다.
다음과 같이 Google Login 버튼이 잘 노출됩니다.
클릭해보면 평소 다른 서비스에서 볼 수 있던 것처럼 구글 로그인 동의 화면으로 이동합니다.
로그인이 성공하면 다음과 같이 구글계정에 등록된 이름이 화면에 노출되는 것을 확인할 수 있습니다.
https://github.com/jojoldu/freelec-springboot2-webservice/issues/549 를 통해 오류사항을 잡았습니다.
(윈도우 환경변수에 userName이 존재하여 올바르게 구글 로그인 이름이 잘나오지 않는 문제였는데 IndexController에서 변수명 수정, Index.mustache에서 관련변수 수정으로 해결했습니다.)
회원가입도 잘되어있는지 확인해보겠습니다.
http://localhost:8080/h2-console에 접속해서 user 테이블을 확인합니다.
데이터베이스에 정상적으로 회원정보가 들어간것까지 확인했습니다. 또한, 권한관리도 잘되는지 확인해보겠습니다.
현재 로그인되니 사용자의 권한은 GUEST입니다. 이상태에서는 Post기능을 전혀 쓸수 없습니다.
실제로 글 등록 기능을 사용하도록 하겠습니다.

실제 테스트를 위한 글쓰기를 진행하면 다음과 같이 403(권한 거부) 에러가 발생한것을 볼 수 있습니다.
그럼 권한을 변경해서 다시 시도해보겠습니다.
h2-console로 가서 사용자의 role을 User로 변경합니다.
SELECT * FROM USER;
update USER set role = 'USER'
세션에는 이미 GUEST인 정보로 저장되어 있으니 로그아웃한 후 다시 로그인하여 세션 정보를 최신 정보로 갱신한 후에 글 등록을 합니다.
그럼 다음과 같이 정상적으로 글이 등록되는 것을 확인할 수 있습니다.

기본적인 구글 로그인, 로그아웃, 회원가입, 권한관리 기능이 모두 구현되었습니다.
자, 이제 조금씩 기능개선을 진행해보겠습니다.

5.4 어노테이션 기반으로 개선하기
일반적인 프로그래밍에서 개선이 필요한 나쁜 코드에는 어떤것이 있을까요?
가장 대표적으로 같은 코드가 반복되는 부분입니다. 같은 코드를 계속해서 복사 & 붙여넣기로 반복하게 만든다면 이후에 수정이 필요할대
모든 부분을 하나씩 찾아가며 수정해야만 합니다. 이렇게 될경우 유지보수성이 떨어질 수 밖에 없으며, 혹시나 수정이 반영되지 않은 반복코드가 있다면
문제가 발생할 수 밖에 없습니다.
자, 그럼 앞서 만든 코드에서 개선할만한 것은 무엇이 있을까요? 필자는 IndexController에서 세션값을 가져오는 부분이라고 생각합니다.
SessionUser user = (SessionUser) httpSession.getAttribute("user");
index 메소드 외에 다른 컨트롤러와 메소드에서 세션값이 필요하면 그때마다 직접 세션에서 값을 가져와야합니다. 같은 코드가 계속해서 반복되는것은
불필요합니다. 그래서 이 부분을 메소드 인자로 세션값을 바로 받을 수 있도록 변경해보겠습니다.
config.auth 패키지에 다음과 같이 @LoginUser 어노테이션을 생성합니다.





 */
