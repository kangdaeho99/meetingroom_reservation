//package com.room.reservation.config.auth;
//
//
//import com.room.reservation.config.auth.dto.OAuthAttributes;
//import com.room.reservation.config.auth.dto.SessionUser;
//import com.room.reservation.domain.user.User;
//import com.room.reservation.domain.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import javax.servlet.http.HttpSession;
//import java.util.Collections;
//
//@RequiredArgsConstructor
//@Service
//public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
//    private final UserRepository userRepository;
//    private final HttpSession httpSession;
//
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
//        OAuth2User oAuth2User = delegate.loadUser(userRequest);
//
////       registrationId : 현재 로그인 진행 중인 서비스를 구분하는 코드입니다.
////       -지금은 구글만 사용하는 불필요한 값이지만, 이후 네이버 로그인 연동 시에 네이버 로그인인지, 구글 로그인인지 구분하기 위해 사용합니다.
//        String registrationId = userRequest.getClientRegistration().getRegistrationId();
////        userNameAttributeName : OAuth2 로그인 진행 시 키가 되는 필드 값을 이야기합니다. Primary Key와 같은 의미입니다.
////        -구글의 경우 기본적으로 코드를 지원하지만, 네이버 카카오 등은 기본 지원하지 않습니다. 구글의 기본코드는 'sub'입니다.
////        이후 네이버 로그인과 구글 로그인을 동시 지원할때 사용합니다.
//        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
//
////        OAuthAttributes: OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담을 클래스입니다.
////        이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용합니다.
////        바로 아래에서 이 클래스의 코드가 나오니 차례로 생성하시면 됩니다.
//        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
//
//        User user = saveOrUpdate(attributes);
////        SessionUser : 세션에 사용자 정보를 저장하기 위한 Dto 클래스입니다.
////        왜 User 클래스를 쓰지 않고 새로 만들어서 쓰는지 뒤이어서 상세하게 설명하겠습니다.
//        httpSession.setAttribute("user", new SessionUser(user));
//
//        return new DefaultOAuth2User(
//                Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
//                attributes.getAttributes(),
//                attributes.getNameAttributeKey());
//    }
//
//    private User saveOrUpdate(OAuthAttributes attributes){
//        User user = userRepository.findByEmail(attributes.getEmail())
//                .map(entity -> entity.update(attributes.getName(), attributes.getPicture()))
//                .orElse(attributes.toEntity());
//        return userRepository.save(user);
//    }
//}
//
//
//
//
//
///*
//스프링 시큐리티 전체 흐름
//---
//스프링 시큐리티는 막강한 인증(Authentication)과 인가(Authorization) (혹은 권한 부여) 기능을 가진 프레임워크입니다.
//사실상 스프링 기반의 애플리케이션에서는 보안을 위한 표준이라고 보면 됩니다.
//인터셉터, 필터 기반의 보안 기능을 구현하는 것보다 스프링 시큐리티를 통해 구현하는 것을 적극적으로 권장하고 있습니다.
//스프링의 대부분 프로젝트들(Mvc, Data, Batch 등등) 처럼 확장성을 고려한 프레임워크다 보니 다양한 요구사항을 손쉽게 추가하고 변경할 수 있습니다.
//이런 손쉬운 설정은 특히나 스프링 부트 1.5 에서 2.0 으로 넘어오면서 더욱 더 강력해졌습니다.
//이번 장에서는 스프링 시큐리티와 OAuth 2.0을 구현한 구글 로그인을 연동하여 로그인 기능을 만들어보겠습니다.
//
//5.1 스프링 시큐리티와 스프링 시키류티 Oauth2 클라이언트
//많은 서비스에서 로그인 기능을 id/password 방식보다는 구글, 페이스북, 네이버 로그인과 같은 소셜 로그인기능을 사용합니다.
//왜 많은 서비스에서 소셜 로그인을 사용할까요?
//필자가 생각하는 이유는 직접 구현할 경우 배보다 배꼽이 커지는 경우가 많기 때문입니다.
//직접 구현하면 당므을 전부 구현해야합니다. OAuth를 써도 구현해야하는 것은 제외했습니다.
//-로그인시보안
//-회원가입시 이메일 혹은 전화번호 인증
//-비밀번호 찾기
//-비밀번호 변경
//-회원정보 변경
//
//OAuth 로그인 구현시 앞선 목록의 것들을 모두 구글, 페이스북, 네이버 등에 맡기면 되니 서비스 개발에 집중할 수 있습니다.
//
//--스프링 부트 1.5 vs 스프링 부트 2.0
//스프링 부트 1.5에서의 OAuth2 연동방법이 2.0 에서는 크게 변경되었습니다.
//하지만 인터넷 자료들(블로그나 깃허브 등)을 보면 설정방법에 크게 차이가 없는 경우를 자주 봅니다.
//이는 spring-security-oauth2-autoconfigure 라이브러리 덕분입니다.
//spring-security-oauth2-autoconfigure
//
//spring-security-oauth2-autoconfigure 라이브러리를 사용할 경우 스프링 부트 2에서도 1.5에서 쓰던 설정을 그대로 사용할 수 있습니다.
//새로운 방법을 쓰기 보다는 기존에 안전하게 작동하던 코드를 사용하는것이 아무래도 더 확실하므로 많은 개발자가 이 방식을 사용해왔습니다.
//하지만 이책에서는 스프링 부트 2 방식인 Spring Security Oauth2 Client 라이브러리를 사용해서 진행합니다.
//이유는 다음과 같습니다.
//-스프링 팀에서 기존 1.5에서 사용되던 spring-security-oauth 프로젝트는 유지상태(maintenance mode)로 결정했으며,
//더는 신규기능은 추가하지 않고 버그 수정 정도의 기능만 추가될 예정, 신규 기능은 새 oauth2 라이브러리에서만 지원하겠다고 선언했습니다.
//-스프링 부트용 라이브러리(starter) 출시
//-기존에 사용하던 방식은 확장 포인트가 적절하게 오픈되어있지 않아 직접 상속하거나 오버라이딩 해야하고 신규 라이브러리의 경우 확장 포인트를 고려해서
//설계된 상태입니다.
//
//그리고 한가지 더 이야기하자면, 이 책 이외에 스프링 부트 2 방식의 자료를 찾고 싶은 경우, 인터넷 자료들 사이에서 다음 두 가지만 확인하면 됩니다.
//먼저 spring-security-oauth2-autoconfigure 라이브러리를 썻는지를 확인하고 application.properties 혹은 application.yml 정보가
//다음과 같이 차이가 있는지 비교해야합니다.
//
//Spring Boot 1.5와 2.0 의 설정차이
//[Spring Boot 1.5 Spring-security configure]
//google :
//    client :
//        clientId : 인증정보
//        clientSecret : 인증정보
//        accessTokenUri : https://accounts.google.com/o/oauth2/token
//        userAuthorizationUri : https://accounts.google.com/o/oauth2/auth
//        clientAuthenticationScheme : form
//        scope : email, profile
//    resource :
//        userInfoUri : https://www.googleapis.com/oauth2/v2/userinfo
//
//[Spring Boot 2.0 Spring-security configure
//spring:
//    security:
//        oauth2:
//            client:
//                clientId:인증정보
//                clientSecret:인증정보
//
//스프링부트 1.5 방식에서는 URL 주소를 모두 명시해야하지만, 2.0 방식에서는 client 인증정보만 입력하면 됩니다.
//1.5 버전에서 직접 입력했던 값들은 2.0 버전으로 오면서 모두 enum으로 대체되었습니다.
//CommonOAuth2Provider라는 enum이 새롭게 추가되어 구글, 깃허브, 페이스북, 옥타(Okta)의 기본 설정값은 모두 여기서 제공합니다.
//
//public enum CommonOAuth2Provider{
//    GOOGLE{
//        @Override
//        public Builder getBuilder(String registrationId){
//            ClientRegistration.Builder builder = getBuilder(registrationId, ClientAuthenticationMethod.BASIC, DEFAULT_REDIRECT_URL);
//            builder.scope("openid", "profile", "email");
//            builder.authorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
//            builder.tokenUri("https://www.googleapis.com/oauth2/v4/token");
//            builder.jwkSetUri("https://www.googleapis.com/oauth2/v3/certs");
//            builder.userInfoUri("https://www.googleapis.com/oauth2/v3/userinfo");
//            builder.userNameAttributeName(IdTokenClaimNames.SUB);
//            builder.clientName("Google");
//            return builder;
//        }123
//    },
//
//    ...
//}
//
//이외에 다른 소셜 로그인(네이버, 카카오 등)을 추가한다면 직접 다 추가해주어야합니다. 이점을 기억해 해당 블로그에서 어떤 방식을 사용하는지 확인후
//참고하면 됩니다.
//그럼 다음절에서 구글 로그인 연동을 시작하겠습니다.
//
//5.2 구글 서비스 등록
//먼저 구글 서비스에 신규서비스여기서 발급된 인증정보(clientId와 clientSecret)를 통해서 로그인 기능과 소셜 서비스 기능을 사용할 수 있으니 무조건 발급받고 시작해야합니다.
//구글 클라우드 플랫폼 주소(http://console.cloud.google.com) 로 이동합니다.
//그리고 다음 그림과 같이 [프로젝트 선택] 탭을 클릭합니다.
//[새 프로젝트] 버튼을 클릭합니다.
//등록될 서비스의 이름을 입력합니다. 원하는 이름으로 지으면 됩니다.
//필자는 freelec-springboot2-webservice로 지었습니다.
//저는 room-reservation-springboot2-webservice 로 지었습니다.
//
//생성이 완료된 프로젝트를 선택하고 왼쪽 메뉴 탭을 클릭해서 API 및 서비스 카테고리로 이동합니다.
//사이드바 중간에 있는 [사용자 인증 정보]를 클릭하고 [사용자 인증 정보 만들기] 버튼을 클릭합니다.
//사용자 인증정보에는 여러 메뉴가 있는데 이 중 이번에 구현할 소셜 로그인은 OAuth 클라이언트 ID로 구현합니다.
//[OAuth 클라이언트 ID] 항목을 클릭합니다.
//바로 클라이언트 ID가 생성되기 전에 동의 화면 구성이 필요하므로 안내에 따라 [동의 화면 구성] 버튼을 클릭합니다.
//동의 화면에서는 3개의 탭이 있는데, 이 중 'OAuth 동의 화면' 탭에서 다음과 같이 각 항목을 작성합니다.
//애플리케이션 이름 : freelec-springboot2-webservice
//지원 이메일 : 본인 이메일
//Google API 범위 : email, profile, openid
//- 애플리케이션 이름 : 구글 로그인 시 사용자에게 노출될 애플리케이션 이름을 이야기합니다.
//- 지원 이메일 : 사용자 동의 화면에서 노출될 이메일 주소입니다. 보통은 서비스의 help 이메일 주소를 사용하지만, 여기서는 독자의 이메일 주소를 사용하시면 됩니다.
//- Google API 의 범위 : 이번에 등록할 구글 서비스에서 사용할 범위 목록입니다. 기본값은 email/profile/openid 이며, 여기서는 딱
//기본범위만 사용중입니다. 이와 다른 정보들도 사용하고 싶다면 범위 추가 버튼으로 추가하면 됩니다.
//동의 화면 구성이 끝났으면 화면 제일 아래에 저장 버튼을 클릭하고 다음과 같이 OAuth 클라이언트 ID 만들기 화면으로 바로 이동합니다.
//화면 아래로 내려가면 다음과 같이 URL 주소를 등록해야합니다. 여기서 [승인된 리디렉션 URL] 항목만 그림과 같이 값을 등록합니다.
//프로젝트 이름:freelec-springboot2-webservice
//애플리케이션 유형 : 웹 애플리케이션
//승인된 리디렉션 URL : http://localhost:8080/login/oauth2/code/google
//승인된 리디렉션 URL
//- 서비스에서 파라미터로 인증정보를 주었을때 인증이 성공하면 구글에서 리다이렉트할 URL입니다.
//- 스프링부트2 버전의 시큐리티에서는 기본적으로 {도메인}/login/oauth2/code/{소셜서비스코드} 로 리다이렉트 URL을 지원하고 있습니다.
//- 사용자가 별도로 리다이렉트 URL을 지원하는 Controller를 만들 필요가 없습니다.
//    시큐리티에서 이미 구현해놓은 상태입니다.
//- 현재는 개발 단계이므로 http://localhost:8080/login/oauth2/code/google 로만 등록합니다.
//- AWS 서버에 배포하게 되면 localhost 외에 추가로 주소를 추가해야하며, 이건 이후 단계에서 진행하겠습니다.
//생성 버튼을 클릭하면 다음 그림과 같이 생성된 클라이언트 정보를 볼 수 있고 생성된 애플리케이션을 클릭하면 그림과 같이 인증정보를 볼 수 있습니다.
//클라이언트 ID(clientID)와 클라이언트 보안비밀코드(clientSecret)를 프로젝트에서 설정하겠습니다.
//
//application-oauth를 등록합니다.
//[\room_reservation\src\main\resources\application-oauth.properties]
//4장에서 만들었던 application.properties 가 있는 src/main/rsources/ 디렉토리에 application-oauth.properties 파일을 생성합니다.
//그리고 해당 파일에 클라이언트 ID와 클라이언트 보안비밀코드를 다음과 같이 등록합니다.
//spring.security.oauth2.client.registration.google.client-id =
//spring.security.oauth2.client.registration.google.client-secret =
//spring.security.oauth2.client.registration.google.scope = profile, email
//
//- 많은 예제에서는 이 scope를 별도로 등록하지 않고 있습니다.
//- 기본값이 openid, profile, email 이기 때문입니다.
//- 강제로 profile, email을 등록한 이유는 openid라는 scope가 있으면 open id provider로 인식하기 때문입니다.
//- 이렇게 되면 OpenIdProvider인 서비스(구굴)와 그렇지 않은 서비스(네이버/카카오 등) 로 나눠서 각각 OAuth2Service를 만들어야합니다.
//- 하나의 OAuth2Service로 사용하기 위해 일부러 openid scope를 뺴고 등록합니다.
//
//스프링 부트에서는 properties의 이름을 application-xxx.properties로 만들면 xxx라는 이름의 profile이 생성되어 이를 통해 관리할 수 있습니다.
//즉, profile=xxx라는 식으로 호출하면 해당 properties의 설정들을 가져올 수 있습니다. 호출하는 방식은 여러 방식이 있지만 이 책에서는
//스프링 부트의 기본 설정 파일인 application.properties에서 application-oauth.properties를 포함하도록 구성합니다.
//application.properties에 다음과 같이 코드를 추가합니다.
//-spring.profiles.include=oauth
//이제 이 설정값을 사용할 수 있게 되었습니다.
//
//.gitignore 등록
//구글 로그인을 위한 클라이언트 ID와 클라이언트 보안 비밀은 보안이 중요한 정보들입니다.
//이들이 외부에 노출될 경우 언제든 개인정보를 가져갈 수 있는 취약점이 될 수 있습니다.
//이 책으로 진행중인 독자는 깃허브와 연동하여 사용하다 보니 application-oauth.properties 파일이 깃허브에 올라갈 수 있습니다.
//보안을 위해 깃허브에 application-oauth.properties 파일이 올라가는 것을 방지하겠습니다.
//1장에서 만들었던
//[\room_reservation\.gitignore]에 다음과 같이 한줄의 코드를 추가합니다.
//이제 이 설정값을 사용할 수 있게 되었습니다.
//추가한 뒤 커밋했을때 커밋파일 목록에 application-oauth.properties가 나오지 않으면 성공입니다.
//만약 .gitignore에 추가했음에도 여전히 커밋 목록에 노출된다면 이는 Git의 캐시문제 때문입니다.
//이에 대한 해결책은 필자의 블로그를 참고하시길 바랍니다.
//.gitignore가 작동하지 않을때 대처법(https://jojoldu.tistory.com/307) 를 확인합니다.(나는 여기서 .gitignore가 작동하면
//특유의 색깔이 바뀌기도하고, git에서 커밋전 아예 아무것도 안뜨게되는데 이것을 하지 않아 clientid를 삭제하고 다시 만들었다. 처음에 잘 설정해야합니다.)
//
//5.3 구글 로그인연동
//구글의 로그인 인증정보를 발급받았으니 프로젝트 구현을 진행하겠습니다.
//먼저 사용자 정보를 담당할 도메인인 User클래스를 생성합니다.
//패키지는 domain 아래에 user 패키지를 생성합니다.
//[\src\main\java\com\room\reservation\domain_user\User.java]
//
//[\src\main\java\com\room\reservation\domain_user\Role.java]
//각 사용자의 권한을 관리할 Enum 클래스 Role을 생성합니다.
//
//[\src\main\java\com\room\reservation\domain_user\UserRepository.java]
//마지막으로 User의 CRUD를 책임질 UserRepository도 생성합니다.
//UserEntity 관련 코드를 모두 작성했으니 본격적으로 시큐리티 설정을 진행하겠습니다.
//
//---스프링 시큐리티 설정---
//먼저 build.gradle에 스프링 시큐리티 관련 의존성 하나를 추가합니다.
//
////스프링 시큐리티 관련 의존성 추가
//implementation('org.springframework.boot:spring-boot-starter-oauth2-client')
//
//build.gradle 설정이 끝났으면 OAuth 라이브러리를 이용한 소셜로그인 설정 코드를 작성합니다.
//config.auth 패키지를 생성합니다. 앞으로 시큐리티 관련 클래스는 모두 이곳에 담는다고 보면 됩니다.
//[\src\main\java\com\room\reservation\config\auth]
//
//설정 코드 작성이 끝났다면 CustomOAuth2UserService 클래스를 생성합니다.
//[\src\main\java\com\room\reservation\config\auth\CustomOAuth2UserService.java]
//이 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원합니다.
//구글 사용자 정보가 업데이트 되었을 떄를 대비하여 update 기능도 같이 구현되었습니다.
//사용자의 이름이나 프로필 사진이 변경되면 User Entity에도 반영됩니다.
//CustomOAuth2UserService 클래스까지 생성되었다면 OAuthAttributes 클래스를 생성합니다.
//필자의 경우 OAuthAttributes는 Dto로 보기 때문에 config.auth.dtio 패키지를 만들어 해당 패키지에 생성했습니다.
//[\src\main\java\com\room\reservation\config\auth\dto\OAuthAttributes.java]
//
//이제는 config.auth.dto 패키지에 SessionUser 클래스를 추가합니다.
//[\src\main\java\com\room\reservation\config\auth\dto\SessionUser.java]
//SessionUser에는 인증된 사용자 정보만 필요합니다. 그 외에 필요한 정보들은 없으니 name, email, picture만 필드로 선언합니다.
//코드가 다 작성되었다면 앞서 궁금했던 내용을 잠시 알아보겠습니다.
//
//왜 User 클래스를 사용하면 안되나요? (CustomOAuth2UserService에서 보면  httpSession.setAttribute("user", new SessionUser(user));
//로 SessionUser를 사용한 것을 볼 수 있습니다.
//만약 User 클래스를 그대로 사용했다면 다음과 같은 에러가 발생합니다. 다음 내용을 읽기 전에 먼저 생각해보면 좋겠습니다.
//Failed to convert from type [java.lang.Object] to type [byte[]] for value 'com.room.reservation.domain.user.user@!#!@#
//이는 세션에 저장하기 위해 User 클래스를 세션에 저장하라겨 하니, User 클래스에 직렬화를 구현하지 않았다는 의미의 에러입니다.
//그럼 오류를 해결하기 위해 User 클래스에 직렬화 코드를 넣으면 될까요? 그것에 대해서는 생각해볼것이 많습니다.
//이유는 User 클래스가 엔티티이기 때문입니다. 엔티티 클래스에는 언제 다른 엔티티와 관계가 형성될지 모릅니다.
//예를들어 @OneToMany, @ManyToMany 등 자식 엔티티를 갖고있다면 직렬화 대상에 자식들까지 포함되니 성능 이슈, 부수효과가 발생할 확률이 높습니다.
//그래서 직렬화 기능을 가진 세션 Dto를 하나 추가로 만드는 것이 이후 운영 및 유지보수 때 많은 도움이 됩니다.
//모든 시큐리티 설정이 끝났습니다. 그럼 로그인 기능을 한번 테스트 해보겠습니다.
//
//로그인테스트
//스프링 시큐리티가 잘 적용되었는지 확인하기 위해 화면에 로그인 버튼을 추가해보겠습니다.
//index.mustache에 로그인 버튼과 로그인 성공 시 사용자 이름을 보여주는 코드입니다.
//[\src\main\resources\templates\index.mustache]
//
//[\src\main\java\com\room\reservation\web\IndexController.java] 그 이후 Index.mustache에서 uerName을 사용할 수 있게
//IndexController에서 userName을 model에 저장하는 코드를 작성합니다.
//
//그럼 한번 프로젝트를 실행해서 테스트해 보겠습니다.
//다음과 같이 Google Login 버튼이 잘 노출됩니다.
//클릭해보면 평소 다른 서비스에서 볼 수 있던 것처럼 구글 로그인 동의 화면으로 이동합니다.
//로그인이 성공하면 다음과 같이 구글계정에 등록된 이름이 화면에 노출되는 것을 확인할 수 있습니다.
//https://github.com/jojoldu/freelec-springboot2-webservice/issues/549 를 통해 오류사항을 잡았습니다.
//(윈도우 환경변수에 userName이 존재하여 올바르게 구글 로그인 이름이 잘나오지 않는 문제였는데 IndexController에서 변수명 수정, Index.mustache에서 관련변수 수정으로 해결했습니다.)
//회원가입도 잘되어있는지 확인해보겠습니다.
//http://localhost:8080/h2-console에 접속해서 user 테이블을 확인합니다.
//데이터베이스에 정상적으로 회원정보가 들어간것까지 확인했습니다. 또한, 권한관리도 잘되는지 확인해보겠습니다.
//현재 로그인되니 사용자의 권한은 GUEST입니다. 이상태에서는 Post기능을 전혀 쓸수 없습니다.
//실제로 글 등록 기능을 사용하도록 하겠습니다.
//
//실제 테스트를 위한 글쓰기를 진행하면 다음과 같이 403(권한 거부) 에러가 발생한것을 볼 수 있습니다.
//그럼 권한을 변경해서 다시 시도해보겠습니다.
//h2-console로 가서 사용자의 role을 User로 변경합니다.
//SELECT * FROM USER;
//update USER set role = 'USER'
//세션에는 이미 GUEST인 정보로 저장되어 있으니 로그아웃한 후 다시 로그인하여 세션 정보를 최신 정보로 갱신한 후에 글 등록을 합니다.
//그럼 다음과 같이 정상적으로 글이 등록되는 것을 확인할 수 있습니다.
//
//기본적인 구글 로그인, 로그아웃, 회원가입, 권한관리 기능이 모두 구현되었습니다.
//자, 이제 조금씩 기능개선을 진행해보겠습니다.
//
//5.4 어노테이션 기반으로 개선하기
//일반적인 프로그래밍에서 개선이 필요한 나쁜 코드에는 어떤것이 있을까요?
//가장 대표적으로 같은 코드가 반복되는 부분입니다. 같은 코드를 계속해서 복사 & 붙여넣기로 반복하게 만든다면 이후에 수정이 필요할대
//모든 부분을 하나씩 찾아가며 수정해야만 합니다. 이렇게 될경우 유지보수성이 떨어질 수 밖에 없으며, 혹시나 수정이 반영되지 않은 반복코드가 있다면
//문제가 발생할 수 밖에 없습니다.
//자, 그럼 앞서 만든 코드에서 개선할만한 것은 무엇이 있을까요? 필자는 IndexController에서 세션값을 가져오는 부분이라고 생각합니다.
//SessionUser user = (SessionUser) httpSession.getAttribute("user");
//index 메소드 외에 다른 컨트롤러와 메소드에서 세션값이 필요하면 그때마다 직접 세션에서 값을 가져와야합니다. 같은 코드가 계속해서 반복되는것은
//불필요합니다. 그래서 이 부분을 메소드 인자로 세션값을 바로 받을 수 있도록 변경해보겠습니다.
//
//[\src\main\java\com\room\reservation\config\auth\LoginUser.java]
//config.auth 패키지에 다음과 같이 @LoginUser 어노테이션을 생성합니다.
//
//그리고 같은 위치에 LoginUserArgumentResolver를 생성합니다. Login-UserArgumentResolver라는 HandlerMethodArgumentResolver 인터페이스를 구현한 클래스입니다.
//HandlerMethodArgumentResolver는 한가지 기능을 지원합니다. 바로 조건에 맞는 경우 메소드가 있다면 HandlerMethodArgumentResolver의
//구현체가 지정한 값으로 해당 메소드의 파라미터로 넘길 수 있습니다.
//자세한 사용법은 직접 만들면서 배워보겠습니다.
//[\src\main\java\com\room\reservation\config\auth\LoginUserArgumentResolver.java]를 생성합니다.
//
//@LoginUser를 사용하기 위한 환경은 구성되었습니다.
//자 이제 이렇게 생성된 LoginUserArgumentResolver가 스프링에서 인식될수 있도록 WebMvcConfigurer에 추가하겠습니다.
//config패키지에 WebConfig 클래스를 생성하여 다음과 같이 설정을 추가합니다.
//[\src\main\java\com\room\reservation\config\WebConfig.java]
//
//HandlerMethodArgumentResolver는 항상 WebMvcConfigurer의 addArgumentResolvers()를 통해 추가해야합니다.
//다른 Handler-MethodArgumentResolver가 필요하다면 같은 방식으로 추가해주면 됩니다.
//최종적으로 패키지 구조는 다음과 같이 됩니다.
//[config/auth/dto] [config/auth/CustomOAuth2UserService, LoginUser, LoginUserArgumentResolver, SecurityConfig]
//
//모든 설정이 끝났으니 처음 언급한대로 IndexController의 코드에서 반복되는 부분들을 모두 @LoginUser로 개선하겠습니다.
//[\src\main\java\com\room\reservation\web\IndexController.java]
//
//5.5 세션저장소로 데이터베이스 사용하기
//추가 개선을 해보겠습니다. 지금 우리가 만든 서비스는 애플리케이션을 재실행하면 로그인이 풀립니다.
//왜 그럴까요? 이는 세션이 내장 톰캣의 메모리에 저장되기 때문입니다. 기본적으로 세션은 실행되는 WAS의 메모리에서 저장되고
//호출됩니다. 메모리에 저장되다 보니 내장톰캣처럼 애플리케이션 실행시 실행되는 구조에선 항상 초기화가 됩니다.
//즉, 배포할때마다 톰캣이 재시작되는 것입니다.
//이 외에도 한가지 문제가 더 있습니다. 2대 이상의 서버에서 서비스하고있다면 톰캣마다 세션동기화 설정을 해야합니다.
//그래서 실제 현업에서는 세션 저장소에 대해 다음의 3가지 중 한가지를 선택합니다.
//(1) 톰캣 세션을 사용합니다.
//-일반적으로 별다른 설정을 하지 않을때 기본적으로 선택되는 방식입니다.
//-이렇게 될경우 톰캣(WAS)에 세션이 저장되기 때문에 2대 이상의 WAS가 구동되는 환경에서는 톰캣들 간의 세션 공유를 위한 추가 설정이 필요합니다.
//(2) MySQL과 같은 데이터베이스를 세션 저장소로 사용합니다.
//-여러 WAS 간의 공용 세션을 사용할 수 있는 가장 쉬운 방법입니다.
//-많은 설정이 필요없지만, 결국 로그인 요청마다 DB IO가 발생하여 성능상 이슈가 발생할 수 있습니다.
//-보통 로그인 요청이 많이 없는 백오피스, 사내 시스템 용도에서 사용합니다.
//(3) Redis, Memcached와 같은 메모리 DB를 세션 저장소로 사용합니다.
//- B2C 서비스에서 가장 많이 사용하는 방식입니다.
//- 실제 서비스로 사용하기 위해서는 Embedded Redis와 같은 방식이 아닌 외부 메모리 서버가 필요합니다.
//
//여기서는 두번쨰 방식인 데이터베이스를 세션 저장소로 사용하는 방식을 선택하여 진행하겠습니다. 선택한 이유는 설정이 간단하고 사용자가 많은 서비스가 아니며
//비용절감을 위해서입니다.
//이후 AWS에서 이 서비스를 배포하고 운영할때를 생각하면 레디스와 같은 메모리 DB를 사용하기는 부담스럽습니다. 왜냐하면, 레디스와 같은 서비스(엘라스틱 캐시)에
//별도로 사용료를 지불해야하기 때문입니다. 사용자가 없는 현재 단계에서는 데이터베이스로 모든 기능을 처리하는게 부담이 적습니다. 만약 본인이 운영중인 서비스가
//커진다면 한번 고려해보고, 이 과정에서는 데이터베이스를 사용하겠습니다.
//
//-spring-session-jdbc 등록
//먼저 build.gradle에 다음과 같이 의존성을 등록합니다. spring-session-jdbc 역시 현재 상태에선 바로 사용할 수 없습니다.
//spring web, spring jpa를 사용했던것과 마찬가지로 의존성이 추가되어 있어야 사용할 수 있습니다.
//build.gradle에 spring-session-jdbc를 추가하겠습니다. [ compile('org.springframework.session:spring-session-jdbc') ]
//그리고 application.properties에 세션저장소를 jdbc로 선택하도록 코드를 추가합니다. 설정은 다음코드가 전부입니다.
//이외에 설정할 것이 없습니다. spring.session.store-type=jdbc
//모두 변경하였으니 다시 애플리케이션을 실행해서 로그인을 테스트한뒤, h2-console로 접속합니다.
//h2-console을 보면 세션을 위한 테이블 2개(SPRING_SESSION, SPRING_SESSION_ATTRIBUTES)가 생성된 것을 볼 수 있습니다.
//JPA로 인해 세션 테이블이 자동생성되었기 때문에 별도로 해야할일은 없습니다.방금 로그인했기 때문에 한개의 세션이 등록돼있는 것을 볼 수 있습니다.
//세션 저장소를 데이터베이스로 교체했습니다. 물론 지금은 기존과 동일하게 스프링을 재시작하면 세션이 풀립니다.
//이유는 H2 기반으로 스프링이 재실행될때 H2도 재시작되기 때문입니다. 이후 AWS로 배포하게되면 AWS의 데이터베이스 서비스인
//RDS(Relational DatabaseService)를 사용하게 되니 이때부터는 세션이 풀리지 않습니다. 그 기반이 되는 코드를 작성한 것이니 걱정하지말고
//다음 과정을 진행하면 됩니다.
//
//5.6 네이버 로그인
//마지막으로 네이버 로그인을 추가해보겟습니다.
//
//네이버 API 등록
//https://developers.naver.com/apps/#/register?api=nvlogin
//다음과 같이 각 항목을 채웁니다.
//
//[사용API, 애플리케이션이름]
//애플리케이션 이름 : room-reservation-springboot2-webservice
//사용API : 네이버 아이도로 로그인에 회원이름, 이메일, 프로필 사진을 필수로 체크합니다.
//
//회원이름, 이메일, 프로필 사진은 필수이며 추가정보는 필요한 경우 선택할 수 있습니다.
//아래로 내려가서 구글에서와 마찬가지로 URL을 등록하며 됩니다.
//
//[로그인 오픈 API 서비스 환경]
//서비스 환경 : PC 웹
//서비스 URL : http://localhost:8080/
//네이버아디로 로그인 Callback URL(최대 5개) : http://localhost:8080/login/oauth2/code/naver
//
//서비스 URL은 필수입니다. 여기서는 localhost:8080 으로 등록합니다.
//callBack URL은 구글에서 등록한 리디렉션 URL과 같은 역할을 합니다.
//여기서는 /login/oauth2/code/naver로 등록합니다.
//등록을 완료하면 다음과 같이 ClientID와 ClientSecret가 생성됩니다.
//
//[네이버 서비스 등록완료.]
//해당 키값들을 application-oauth.properties에 등록합니다.
//네이버에서는 스프링 시큐리티를 공식 지원하지 않기때문에 그동안 Common-OAuth2Provider에서 해주던 값들도 전부 수동으로 입력해야합니다.
//[\src\main\resources\application-oauth.properties]
//코드 작성은 [https://github.com/jojoldu/freelec-springboot2-webservice/blob/master/src/main/resources/application-oauth.properties]
//레퍼런스에서 복사해왔습니다.
//# user_name_attribute=response : 기준이 되는 user_name이름을 네이버에서는 response로 해야합니다.
//# 이유는 네이버의 회원조회시 반환되는 JSON 형태 떄문입니다.
//
//네이버 오픈 API의 로그인 회원 결과는 다음과 같습니다.
//{
//    "resultcode": "00",
//    "message":"success",
//    "response":{
//        "email": "openapi@naver.com",
//        "nickname": OpenAPI",
//        "profile_image" : "https://ssl.pstatic.net/static/pwe/address/nodata_33x33.gif",
//        "age": "40-49",
//        "gender": "F",
//        "id": "32742776",
//        "name": "오픈API",
//        "birthday" : "10-01"
//    }
//}
//스프링 시큐리티에선 하위필드를 명시할 수 없습니다.
//최상위 필드들만 user_name으로 지정 가능합니다. 하지만 네이버의 응답값 최상위 필드는 resultCode, message, response 입니다.
//이러한 이유로 스프링 시큐리티에서 인식 가능한 필드는 저 3개중에 골라야합니다. 본문에서 담고 있는 response를 user_name으로 지정하고
//이후 자바코드로 response의 id를 user_name으로 지정하겠습니다.
//
//스프링 시큐리티 설정등록
//구글 로그인을 등록하면서 대부분 코드가 확장성 있게 작성되었다 보니 네이버는 쉽게 등록가능합니다.
//OAuthAttributes에 다음과 같이 네이버인지 판단하는 코드와 네이버 생성자만 추가해주면 됩니다.
//[\src\main\java\com\room\reservation\config\auth\dto\OAuthAttributes.java]
//public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes){
//함수에 네이버 관련 정보를 추가하고
//네이버라면
//private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
//를 실행시키도록 합니다.
//
//마지막으로 index.mustache에 네이버 로그인 버튼을 추가합니다.
//
//자 이제 메인화면을 확인해보면 네이버 버튼이 활성화 된것을 볼 수 있으며 네이버 로그인 버튼을 누르면 다음과 같이 동의 화면이 등장합니다.
//그럼 다음과 같이 로그인이 성공하는 것을 확인할 수 있습니다.
//네이버 로그인까지 성공했습니다!
//
//5.7 기존 테스트에 시큐리티 적용하기
//마지막으로 기존 테스트에 시큐리티 적용으로 문제가 되는 부분들을 해결해보겠습니다.
//문제가 되는 부분들은 대표적으로 다음과 같은 이유 때문입니다.
//기존에는 바로 API를 호출할 수 있어 테스트 코드 역시 바로 API를 호출하도록 구성하였습니다.
//하지만, 시큐리티 옵션이 활성화되면 인증된 사용자만 API를 호출할 수 있습니다.
//기존의 API 테스트 코드들이 모두 인증에 대한 권한을 받지 못하였으므로, 테스트코드마다 인증한 사용자가 호출한것처럼 작동하도록 수정하겠습니다.
//인텔리제이 오른쪽 위에 [Gradle] 탭을 클릭합니다. [Tasks -> veritification -> test] 를 차례로 선택해서 전체테스트를 수행합니다.
//test를 실행해보면 다음과 같이 롬복을 이용한 테스트 외에 스프링을 이용한 테스트는 모두 실패하는 것을 확인할 수 있습니다.
//그 이유를 하나씩 확인해보겠습니다.
//[문제1. CustomOAuth2UserService를 찾을 수 없음]
//첫번째 실패테스트인 "hello가_리턴되다"의 메세지를 보면
//NoSuchBeanDefinitionException: No qualifying bean of type 'com.room.reservation.config.auth.CustomOAuth2UserService'
//available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
//라는 메세지가 등장합니다.
//이는 CustomOAuth2UserService를 생성하는데 필요한 소셜 로그인 관련 설정값들이 없기 때문에 발생합니다.
//그렇다면! 이상합니다. 분명 application-oauth.properties에 설정값들을 추가했는데 왜 설정이 없다고할까요?
//이는 src/main 환경과 src/test 환경의 차이 때문입니다. 둘은 본인만의 환경 구성을 가집니다.
//다만, src/main/resources/application.properties가 테스트코드를 수행할때도 적용되는 이유는 test에 application.properties
//가 없으면 main의 설정을 그대로 가져오기 때문입니다. 다만, 자동으로 가져오는 옵션의 범위는 application.properties 파일까지입니다.
//즉, application-oauth.properties는 test에 파일이 없다고 가져오는 파일은 아니라는 점입니다.
//이 문제를 해결하기 위해 테스트 환경을 위한 application.properties를 만들겠습니다. 실제로 구글연동까지 진행할것은 아니므로
//가짜 설정값을 등록합니다.
//[\src\test\resources\application.properties]에 설정값을 추가합니다.
//
//다시 그레이들로 테스트를 수행해보면 다음과 같이 7개의 실패테스트가 4개로 줄어들었습니다.
//근데 저같은경우 그냥 원래 4개가 성공하고 4개는 실패했어서 변화가 없었습니다.
//
//문제2. 302 Status Code
//두번째로 "Posts_등록된다" 테스트 로그를 확인해봅니다.
//expected:<[200 OK]> but was:<[302 FOUND]>
//필요:200 OK
//실제   :302 FOUND
//
//응답의 결과로 200(정상 응답) Status Code를 원했는데 결과는 302(리다이렉션 응답) Status Code가 와서 실패했습니다.
//이는 스프링 시큐리티 설정 때문에 인증되지 않은 사용자의 요청은 이동시키기 때문입니다.
//그래서 이런 API 요청은 임의로 인증된 사용자를 추가하여 API만 테스트해볼 수 있겠습니다.
//어려운 방법은 아니며, 이미 스프링 시큐리티에서 공식적으로 방법을 지원하고 있으므로 바로 사용해보겠습니다.
//스프링 시큐리티 테스트를 위한 여러 도구를 지원하는 spring-security-test를 build.gradle에 추가합니다.
//[\room_reservation\build.gradle]
//
//그리고 PostApiControllerTest의 2개 테스트 메소드에 다음과 같이 임의 사용자 인증을 추가합니다.
//[\src\test\java\com\room\reservation\web\PostsApiControllerTest.java]
//@WithMockUser(roles="USER") 를 posts_등록된다. Posts_수정된다에 어노테이션으로 추가합니다.
//
//이정도만하면 테스트가 될것같지만, 실제로 작동하진 않습니다.
//@WithMockUser가 MockMvc에서만 작동하기 때문입니다.
//현재 PostsApiControllerTest는 @SpringBootTest로만 되어있으며 MockMvc를 전혀 사용하지 않습니다.
//그래서 @SpringBootTest에서 MockMvc를 사용하는 방법을 소개합니다.  코드를 다음과 같이 변경합니다.
//[\src\test\java\com\room\reservation\web\PostsApiControllerTest.java]에서 수정합니다.
//
//자, 그리고 다시 전체테스트를 수행해보겠습니다.
//이제 Posts테스트도 정상적으로 수행되었습니다!. 마지막으로 남은 테스트들을 정리해보겠습니다.
//
//[문제3 @WebMvcTest에서 CustomOAuth2User를 찾을 수 없음]
//제일 앞에서 발생한 "Hello가 리턴된다" 테스트를 확인해봅니다.
//그럼 첫번째로 해결한 것과 동일한 메세지인  No qualifying bean of type 'com.room.reservation.config.auth.CustomOAuth2UserService'
//가 나옵니다.
//이 문제는 왜 발생했을까요?
//HelloControllerTest는 1번과는 조금 다른점이 있습니다. 바로 @WebMvcTest를 사용한다는 점입니다.
//1번을 통해 스프링 시큐리티 설정은 잘작동했지만, @WebMvcTest는 CustomOAuth2UserService를 스캔하지 않기 때문입니다.
//@WebMvcTest는 WebSecurityConfigurerAdapter, WebMvcConfigurer를 비롯한 @ControllerAdvice, @Controller를 읽습니다.
//즉, @Repository, @Service, @Component는 스캔대상이 아닙니다.
//그러니 SecurityConfig는 읽었지만, SecurityConfig를 생성하기 위해 필요한 CustomOAuth2UserService는 읽을 수가 없어 앞에서와 같이 에러가
//발생한 것입니다. 그래서 이 문제를 해결하기 위해 다음과 같이 스캔 대상에서 SecurityConfig를 제거합니다.
//[\src\test\java\com\room\reservation\web\HelloControllerTests.java]
//
//언제 삭제될지 모르니 사용하지 않으시는걸 추천합니다. 그리고 여기서도 마찬가지로 @WithMockUser를 사용해서
//가짜로 인증된 사용자를 생성합니다.
//[\src\test\java\com\room\reservation\web\HelloControllerTests.java]
//@WithMockUser(roles="USER")
//public void hello가_리턴되다() throws Exception{
//@WithMockUser(roles="USER")
//public void helloDTO가_리턴되다() throws Exception{
//
//이렇게 한뒤 다시 테스트를 돌려보면 다음과 같은 추가 에러가 발생합니다.
//java.lang.IllegalArgumentException: JPA metamodel must not be empty!
//
//이 에러는 @EnableJpaAuditing 으로 인해 발생합니다.
//@EnableJpaAuditing를 사용하기 위해선 최소 하나의 @Entity 클래스가 필요합니다.
//@WebMvcTest이다 보니 당연히 없습니다.
//@EnableJpaAuditing가 @SpringBootApplication와 함께 있다보니 @WebMvcTest에서도 스캔하게 되었습니다.
//그래서 @EnableJpaAuditing과 @SpringBootApplication 둘을 분리하겠습니다.
//Application.java에서 @EnableJpaAuditing을 제거합니다.
//[\src\main\java\com\room\reservation\Application.java]
//
//config패키지에 JpaConfig를 생성하여 @EnableJpaAuditing를 추가합니다.
//[\src\main\java\com\room\reservation\config\JpaConfig.java]
//@WebMvcTest는 일반적인 @Configuration은 스캔하지 않습니다.
//그리고 다시 전체 테스트를 수행해봅니다.
//
//모든 테스트를 통과했습니다!
//앞의 과정을 토대로 스프링 시큐리티 적용으로 깨진 테스트를 적절하게 수정할 수 있게 되엇습니다.
//우리는 앞서 인텔리제이로 스프링 부트 통합 개발환경을 만들고 테스트와 JPA로 데이터를 처리하고 머스테치로 화면을 구성했으며
//시큐리티와 Oauth로 인증과 권한을 배워보며 간단한 게시판을 모두 완성했습니다.
//예전만 하더라도 스프링 시큐리티를 사용하기가 쉽지 않았습니다.
//하지만 계속 버전이 상향되어 최근 버전에서는 확장하기 쉬워졌습니다.
//꼭 필자의 선택인 스프링 부트 시큐리티 2.0을 쓰지 않고 1.5를 사용해도되지만, 언젠가는 업데이트를 해야만 합니다.
//이제 AWS를 이용해 나만의 서비스를 직접 배포하고 운영하는 과정을 진행하겠습니다.
//
//
//
//
//
//
//
//
// */
