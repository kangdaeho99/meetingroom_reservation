package com.room.reservation.config;

import com.room.reservation.config.auth.LoginUserArgumentResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@RequiredArgsConstructor
@Configuration
public class WebConfig  implements WebMvcConfigurer {
    private final LoginUserArgumentResolver loginUserArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers){
        argumentResolvers.add(loginUserArgumentResolver);
    }
}

/*
HandlerMethodArgumentResolver는 항상 WebMvcConfigurer의 addArgumentResolvers()를 통해 추가해야합니다.
다른 Handler-MethodArgumentResolver가 필요하다면 같은 방식으로 추가해주면 됩니다.
최종적으로 패키지 구조는 다음과 같이 됩니다.
[config/auth/dto] [config/auth/CustomOAuth2UserService, LoginUser, LoginUserArgumentResolver, SecurityConfig]

모든 설정이 끝났으니 처음 언급한대로 IndexController의 코드에서 반복되는 부분들을 모두 @LoginUser로 개선하겠습니다.

 */