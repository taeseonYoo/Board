package com.tae.board.service;

import com.tae.board.domain.Member;
import com.tae.board.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    @Test
    public void 회원가입() throws Exception {
        //given
        Member member = Member.createMember("kim","spring@naver.com",
                "asd123","kino");

        //when
        Long savedId = memberService.join(member);

        //then
        assertEquals(member, memberService.findOne(savedId));
    }

    @Test
    public void 중복_닉네임() throws Exception {
        //given
        Member member1 = Member.createMember("kim","spring@naver.com",
                "asd123","kino");
        Member member2 = Member.createMember("yoo","spring@google.com",
                "asd123","kino");
        //when
        memberService.join(member1);
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        //then
        assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());

    }

    @Test
//    @Rollback(value = false)
    public void 회원가입_롤백X() {
        //given
        Member member1 = Member.createMember("kim","spring@naver.com",
                "1234567","mario");
        Member member2 = Member.createMember("yoo","jpa@google.com",
                "asdfgh","kino");
        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        List<Member> members = memberService.findMembers();
        Assertions.assertThat(members.size()).isEqualTo(2);
    }
}