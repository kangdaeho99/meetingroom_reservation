package com.room.reservation.config.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;




//    과거방식(Security의 Session 관리를
//    @Override
//    protected void configure(HttpSecurity http) throws Exception{
//
//
//        http.csrf()
//                .disable()
//                .headers()
//                .frameOptions()
//                .disable()
//                .and()
//                .authorizeRequests()
//                .antMatchers( "/**",  "/login", "routes/**","/static/**", "/css/**", "/images/**","/js/**","/h2-console/**").permitAll() //해당 페이지들 모두 접근가능
//
//                .antMatchers("/api/v1/**").hasRole(Role.USER.name()) //로그인한 사용자만 가능
//                .anyRequest().authenticated() //그 외의 요청은 권한이 있기만 하면 허용
//                .and()
//                .oauth2Login()
//                .loginPage("/login").failureUrl("/login?error").permitAll()
//                .and()
//                .logout()
//                .logoutSuccessUrl("/")
//                .and()
//                .oauth2Login()
//                .userInfoEndpoint()
//                .userService(customOAuth2UserService);
//
//
//    }

}

/*
EnableWebSecurity : Spring Security 설정들을 활성화시켜줍니다.
csrf().disable().headers().frameOptions().disable() : h2-console 화면을 사용하기 위해 해당 옵션들을 disable합니다.
authorizeRequests : URL 별 권한 관리를 설정하는 옵션의 시작점입니다.
antMatchers : 권한 관리 대상을 지정하는 옵션입니다.
:URL, HTTP 메소드별로 관리가 가능합니다.
:"/"등 지정된 URL등은 permitAll() 옵션을 통해 전체 열람 권한을 주었습니다.
:"/api/v1/**" 주소를 가진 API는 USER 권한을 가진 사람만 가능하도록 했습니다.
anyRequest : 설정된 값들 이외 나머지 URL들을 나타냅니다.
-여기서는 authenticated()을 추가하여 나머지 URL들은 모두 인증된 사용자들에게만 허용하게 합니다.
-인증된 사용자 즉, 로그인한 사용자들을 이야기합니다.
logout().logoutSucessUrl("/") : 로그아웃 기능에 대한 여러 설정의 진입점입니다.
-로그아웃 성공시 / 주소로 이동합니다.
oauth2Login : OAuth 2 로그인 기능에 대한 여러 설정의 진입점입니다.
userInfoEndpoint : OAuth 2 로그인 성공 이후 사용자 정보를 가져올때의 설정들을 담당합니다.
userService : 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록합니다.
-리소스 서버(즉, 소셜 서비스들)에서 사용자 정보를 가져온 상태에서 추가로 진행하고자하는 기능을 명시할 수 있습니다.

설정 코드 작성이 끝났다면 CustomOAuth2UserService 클래스를 생성합니다.
[\src\main\java\com\room\reservation\config\auth\CustomOAuth2UserService.java]
이 클래스에서는 구글 로그인 이후 가져온 사용자의 정보(email, name, picture 등)들을 기반으로 가입 및 정보수정, 세션 저장 등의 기능을 지원합니다.



 */