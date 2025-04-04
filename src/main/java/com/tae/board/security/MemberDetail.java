package com.tae.board.security;

import com.tae.board.domain.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Collections;

public class MemberDetail implements UserDetails {
    private final Member member;

    public MemberDetail(Member member) {
        this.member = member;
    }

    public Member getMember() {
        return member;
    }

    //권한 목록을 반환한다.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    public String getNickname() {
        return member.getNickname();
    }

}
