package com.room.reservation.config;

import com.room.reservation.security.filter.ApiCheckFilter;
import com.room.reservation.security.filter.ApiLoginFilter;
import com.room.reservation.security.handler.MemberLoginSuccessHandler;
import com.room.reservation.security.service.MemberUserDetailsService;
import com.room.reservation.security.util.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * EnableMethodSecurity(prePostEnabled = true) : 어노테이션 기반의 접근 제한을 설정할 수 있도록 하는 설정입니다.
 * SecurityConfig를 사용해서 지정된 URL에 접근제한을 거는것은 번거로운 작업이기에,
 * EnableMethodSecurity의 적용, 접근 제한이 필요한 컨트롤러의 메서드에 @PreAuthorize 적용을 함으로써 적용합니다.
 * PreAuthorize를 이용하기 위해서 prePostEnabled = true 로 설정합니다.
 * PreAuthorized()의 value로는 문자열로 된 표현식을 넣을 수 있습니다.
 * 예시 1. @PreAuthorize("hasRole('ADMIN')")
 * 예시 2. @PreAuthorize("permitAll()")
 * 예시 3. @PreAuthorize("#authMember != null && #authMember.username eq \"hello@hello.com\"")
 * public String preauthorizeOnly(@AuthenticationPrincipal AuthMemberDTO authMember)
 */

@Configuration
@EnableWebSecurity
@Log4j2
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private MemberUserDetailsService memberUserDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 메모리상에 있는 데이터를 이용하는 인증매니저인 InMemoryUserDetailsManager 생성합니다.
     * 이를 통해 DB없이 메모리에 저장시켜서, 로그인할 수 있습니다. 임시의 아이디와 패스워드를 사용하기 위해 생성하는 함수입니다.
     * @return InMemoryUserDetailsManager(user); 메모리에 UserDetailsManager를 저장해주는 객체
     * UserDetailsService 등록시 삭제처리
     */
//    @Bean
//    public InMemoryUserDetailsManager userDetailsService(){
//        UserDetails user = User.builder()
//                .username("user1")
//                .password(passwordEncoder().encode("1111"))
//                .roles("USER")
//                .build();
//
//        log.info("userDetailsService......................");
//        log.info(user);
//
//        return new InMemoryUserDetailsManager(user);
//    }

    /**
     * SecurityFilterChain을 생성하여 Filter를 설정해줍니다.
     * 이 과정을 통해 로그인을 완료하기까지의 과정들을 Control할 수 있습니다.
     * http.authorizeHttpRequests() : 사용자의 권한("USER","ADMIN")에 따라서 접근할 수 있는 페이지를 정합니다.
     * requestMatchers(URL).permitAll() : 모든 사용자가 접근가능
     * requestMatchers(URL).hasRole("USER") : "USER" 권한 사용자가 접근가능
     * http.formLogin() : http.formLogin()는 기본적인 폼 로그인을 구성합니다. 사용자는 인증되지 않은 경우 로그인 페이지로 리디렉션됩니다.
     * TODO : 이후에 http.loginPage() 로 별도의 로그인페이지로 연결
     * http.csrf().disable() : CSRF(Cross-Site Request Forgery) 보호를 비활성화하는 설정입니다. 이는 CSRF 공격을 방지하는 기능을 제거합니다. REST 방식에서 매번 CSRF 토큰의 값을 알아내야하는 불편함이 있기에 disable시키고 진행합니다.
     * http.logout() : 로그아웃 구성을 추가합니다. 사용자는 로그아웃 URL을 통해 세션을 종료할 수 있습니다.
     * TODO : 이후에 http.logoutUrl(), logoutSuccessUrl() 을 통해 별도의 로그아웃 기능 추가
     * http.build() : 구성된 HttpSecurity 객체를 반환합니다.
     * @param http (HttpSecurity)
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info("----------------------filterChain------------------------");
//        http.authorizeHttpRequests()
//                .requestMatchers("/**").permitAll()
//                .requestMatchers("/room/register").hasRole("USER");

        http.formLogin();
        http.csrf().disable();
        http.logout();
        http.oauth2Login().successHandler(successHandler());
        http.rememberMe()
                .tokenValiditySeconds(60*60*24*7)
                .userDetailsService(memberUserDetailsService);

        //Filter 순서 조절 (패스워드 체크 이전 apiCheckFilter()) 실행되도록 설정
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        //AuthenticationManager 설정
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(memberUserDetailsService).passwordEncoder(passwordEncoder());
        //Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        //반드시 필요
        http.authenticationManager(authenticationManager);

        //APILoginFilter
        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login", jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager);
        http.addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
//        http.addFilterBefore(apiLoginFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * OAuth2 소셜로그인이 성공한 이후에 이동할 페이지를 처리해줍니다.
     * @return
     */
    @Bean
    public MemberLoginSuccessHandler successHandler(){
        return new MemberLoginSuccessHandler(passwordEncoder());
    }

    @Bean
    public JWTUtil jwtUtil(){
        return new JWTUtil();
    }
    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter("/room/**/*", jwtUtil());
    }

    /**
     * '/URL'이라는 경로로 접근할때 동작하도록 지정하고, filterChain에서 UsernamePasswordAuthenticationFilter 전에 동작하도록 지정합니다.
     * 프로젝트를 실행하고, '/URL' 을 email 파라미터 없이 전송하면 401 에러가 발생합니다ㅣ
     * @param authenticationManager
     * @return
     * @throws Exception
     */
//    @Bean
//    public ApiLoginFilter apiLoginFilter(AuthenticationManager authenticationManager) throws Exception{
//        ApiLoginFilter apiLoginFilter = new ApiLoginFilter("/api/login");
//        apiLoginFilter.setAuthenticationManager(authenticationManager);
//        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
//
//        return apiLoginFilter;
//    }
}
