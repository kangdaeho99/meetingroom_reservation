package com.room.reservation.config.auth;

import com.room.reservation.config.auth.dto.SessionUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpSession;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

//    supportParameter()
//    :컨트롤러 메서드의 특정 파라미터를 지원하는지 판단합니다.
//    :여기서는 파라미터에 @LoginUser 어노테이션이 붙어 있고, 파라미터 클래스타입이 SessionUser.class 인경우 true를 반환합니다.
    @Override
    public boolean supportsParameter(MethodParameter parameter){
        boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
        boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());
        return isLoginUserAnnotation && isUserClass;
    }

//    resolveArgument()
//    :파라미터에 전달할 객체를 생성합니다.
//    :여기서는 세션에서 객체를 가져옵니다.
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }

}
/*
@LoginUser를 사용하기 위한 환경은 구성되었습니다.
자 이제 이렇게 생성된 LoginUserArgumentResolver가 스프링에서 인식될수 있도록 WebMvcConfigurer에 추가하겠습니다.
config패키지에 WebConfig 클래스를 생성하여 다음과 같이 설정을 추가합니다.

 */