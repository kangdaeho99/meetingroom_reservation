package com.room.reservation.security.service;

import com.room.reservation.entity.Member;
import com.room.reservation.repository.MemberRepository;
import com.room.reservation.security.dto.AuthMemberDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService{

    private final MemberRepository memberRepository;

    /**
     * @RequiredArgsConstructor를 통해 memberRepository를 final 변수의 생성자를 만듭니다.
     * username이 실제로는 member의 email을 의미합니다. 이를 이용하여 findByEmail을 실행합니다.
     * 사용자가 존재하지 않을경우 UserNameNotFoundException이 실행됩니다.
     * Member를 UserDetails타입으로 처리하기 위해 AuthMemberDTO 타입(User타입을 상속)으로 변환합니다.
     * authMemberRole은 Spring Security에서 사용하는 SimpleGrantedAuthority로 변환하고, 'ROLE_' 접두어를 추가하여 생성합니다.
     * @param username the username identifying the user whose data is required.
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("UserDetailsService loadUserByUsername "+ username);

        Optional<Member> result = memberRepository.findByEmail(username, false);

        if(result.isEmpty()){
            throw new UsernameNotFoundException("Check Email or Social ");
        }

        Member member = result.get();

        log.info("-------------------------");
        log.info(member);

        AuthMemberDTO authMember = new AuthMemberDTO(
                member.getEmail(),
                member.getPassword(),
                member.isFromSocial(),
                member.getRoleSet().stream().map(
                        role -> new SimpleGrantedAuthority("ROLE_"+role.name()))
                        .collect(Collectors.toSet()));

        authMember.setNickname(authMember.getNickname());
        authMember.setFromSocial(authMember.isFromSocial());

        return authMember;
    }
}
