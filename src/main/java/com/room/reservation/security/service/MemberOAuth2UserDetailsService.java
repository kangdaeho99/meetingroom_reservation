package com.room.reservation.security.service;

import com.room.reservation.entity.Member;
import com.room.reservation.entity.MemberRole;
import com.room.reservation.repository.MemberRepository;
import com.room.reservation.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * OAuth2UserService
 * 소셜로그인 이후에 로그인 처리가 끝난 결과를 가져와야합니다. OAuth2UserService를 활용합니다.
 * org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService 인터페이스는 우리가 앞에서 일반 로그인에 사용했었던
 * UserDetailsService의 OAuth 버전이라고 생각하면 됩니다.
 * 이를 구현하는 것은 OAuth의 인증결과를 처리한다는 의미입니다.
 * https://docs.spring.io/spring-security/site/docs/6.1.0/api/org/springframework/security/oauth2/client/userinfo/OAuth2UserService.html
 * 위의 링크를 들어가보면, OAuth2UserService를 구현하는데
 * All Known Implementing Classes:
 * DefaultOAuth2UserService, DelegatingOAuth2UserService, OidcUserService
 * 이 3가지의 구현 클래스를 상속(extends)하여 구현할 수 있습니다.
 * 인터페이스를 직접 구현하는대신에 DefaultOAuth2USerService 클래스를 상속해서 구현합니다.
 *
 *
 */
@Log4j2
@Service
@RequiredArgsConstructor
public class MemberOAuth2UserDetailsService extends DefaultOAuth2UserService {

    /**
     *
     */
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * OAuth로 연결한 클라이언트 이름(getClientName()), OAuth2User 객체의 내부에 어떤 값들이 있는지 확인합니다.
     * OAuth2 관련 내용들은 구글에서 프로젝트 등록할때 'API범위'dhk application-oauth.properties와 연관되어 있습니다.
     * 우리는 sub, picture, email, email_veritifed와 같은 항목들이 나옵니다.
     *
     *
     * @param userRequest OAuth2UserRequest ( the user request ) : 현재 어떤 서비스를 통해서 로그인이 이루어져있는지 알아냅니다.
     *                    전달된 값들을 추출할 수 있는 데이터를 Map<String, Object> 형태로 사용합니다.
     *
     *
     * @return OAuth2User, DefaultOAuth2UserService의 loadUser()의 경우 일반적인 로그인과 다르게 OAuth2User 타입의 객체를 반환해야하는데
     *        이를 위해 소셜로그인을 사용할때도 OAuth2User 타입을 AuthMemberDTO 타입으로 사용할 수 있도록 처리해야합니다.
     *        다행히도 OAuth2User 타입은 인터페이스로 설계되어 있으므로 AuthMemberDTO를 implements OAuth2User로 하여 OAuth2User 인터페이스를 구현하도록 수정합니다.
     *        가장 중요한 차이는 OAuth2User는 Map 타입으로 만든 모든 인증결과를 attributes라는 이름으로 가지고 있기 때문에 AuthMemberDTO 역시 attr이라는 변수를 만들어주고
     *        getAttributes() 메서드를 Override 합니다. 이에 맞춰서 MemberOAuth2UserDetailsService 클래스의 loadUser() 의 내부도 수정합니다.
     *
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{
        log.info("-----------------------");
        log.info("userRequest:" + userRequest); //org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest 객체

        String clientName = userRequest.getClientRegistration().getClientName();

        log.info("clientName: "+ clientName); //Google로 출력
        log.info(userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info("======================");
        oAuth2User.getAttributes().forEach((k,v) -> {
            log.info(k + ":" + v); //sub, picture, email, email_verified, EMAIL 등이 출력됩니다.
        });

        String email = null;
        if(clientName.equals("Google")){ //구글소셜로그인
            email = oAuth2User.getAttribute("email");
        }

        log.info("EMAIL: "+ email);

        Member member = saveSocialMember(email);

        /**
         * saveSocialMember() 한 결과로 나오는 member를 이용해서 AuthMemberDTO를 구성합니다.
         * OAuth2User의 모든 데이터는 AuthMemberDTO의 내부로 전달해서 필요한 순간에 사용할 수 있도록 합니다.
         */
        AuthMemberDTO authMemberDTO = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                true,
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList()),
                oAuth2User.getAttributes()
        );
        authMemberDTO.setNickname(member.getNickname());
        return authMemberDTO;
//        return oAuth2User;
//        return super.loadUser(userRequest);

    }

    private Member saveSocialMember(String email){
        //기존에 동일한 이메일로 가입한 회원이 있는 경우에는 그대로 조회만
        Optional<Member> result = memberRepository.findByEmail(email, true);

        if(result.isPresent()){
            return result.get();
        }

        //처음으로 로그인한것이라면, 패스워드는 1111 이름은 그냥 이메일주소로
        Member member = Member.builder().email(email)
                .nickname(email)
                .password(passwordEncoder.encode("1111"))
                .fromSocial(true)
                .build();

        member.addMemberRole(MemberRole.USER);
        memberRepository.save(member);
        return member;
    }
}
