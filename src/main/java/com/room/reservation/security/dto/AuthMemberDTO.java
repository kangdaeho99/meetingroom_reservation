package com.room.reservation.security.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * AuthMemberDTO는 User를 상속하여 부모 클래스인 User 클래스의 생성자를 super를 통해 호출합니다.
 * 부모 클래스인 User 클래스의 생성자를 호출할 수 있는 코드를 만듭니다. (부모 클래스인 User 클래스에 사용자 정의 생성자가 있으므로 반드시 호출해야합니다.)
 * Entity와 DTO 클래스를 별도로 구성시키기 위해 AuthMemberDTO를 생성합니다.
 * AuthMemberDTO는 DTO역할을 수행하면서 Spring Security에서 User 클래스를 extends(상속)하여 인가/인증 작업에 사용할 수 있습니다.
 * password는 부모 클래스를 사용하므로 별도의 멤버변수를 선언하지 않습니다.
 */
@Log4j2
@Getter
@Setter
@ToString
public class AuthMemberDTO extends User implements OAuth2User {
    private String email;

    private String password;

    private String nickname;
    private boolean fromSocial;

    private Map<String, Object> attr;

    public AuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority > authorities, Map<String, Object> attr){
        this(username, password, fromSocial, authorities);
        this.attr = attr;
    }

    public AuthMemberDTO(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority > authorities){
        super(username, password, authorities);
        this.email = username;
        this.password = password;
        this.fromSocial = fromSocial;

    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attr;
    }

    @Override
    public String getName() {
        return null;
    }
}
