package com.room.reservation.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Log4j2
public class SecurityConfig {

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 메모리상에 있는 데이터를 이용하는 인증매니저인 InMemoryUserDetailsManager 생성합니다.
     * 이를 통해 DB없이 메모리에 저장시켜서, 로그인할 수 있습니다. 임시의 아이디와 패스워드를 사용하기 위해 생성하는 함수입니다.
     * @return InMemoryUserDetailsManager(user); 메모리에 UserDetailsManager를 저장해주는 객체
     */
    @Bean
    public InMemoryUserDetailsManager userDetailsService(){
        UserDetails user = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("1111"))
                .roles("USER")
                .build();

        log.info("userDetailsService......................");
        log.info(user);

        return new InMemoryUserDetailsManager(user);
    }

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
        http.authorizeHttpRequests()
                .requestMatchers("/**").permitAll()
                .requestMatchers("/room/register").hasRole("USER");
        http.formLogin();
        http.csrf().disable();
        http.logout();

        return http.build();
    }
}
