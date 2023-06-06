package com.room.reservation.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Member extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mno;

    private String email;

    private String password;

    private String nickname;

    private boolean fromSocial;

    /**
     * ElementCollection을 활용하여 Member 객체의 일부로만 사용합니다.
     */
    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<MemberRole> roleSet = new HashSet<MemberRole>();

    public void addMemberRole(MemberRole memberRole){
        roleSet.add(memberRole);
    }


}
