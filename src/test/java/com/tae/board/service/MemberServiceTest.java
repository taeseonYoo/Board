package com.tae.board.service;

import com.tae.board.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;

    @Test
    public void 회원가입_성공(){
        //given
        Member member = Member.createMember("kim","spring@naver.com",
                "password123","kinopio");
        //when
        Long savedId = memberService.join(member);

        //then
        assertThat(memberService.findOne(savedId)).isEqualTo(member);
    }

    @Test
    public void 중복_닉네임_회원가입_실패(){
        //given
        Member member1 = Member.createMember("kim","kim@spring.io",
                "12345678","nickname");
        Member member2 = Member.createMember("lee","lee@spring.io",
                "12345678","nickname");
        //when
        memberService.join(member1);
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> memberService.join(member2));

        //then
        assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    }

    @Test
    public void 중복_이메일_회원가입_실패() {
        //given
        Member member1 = Member.createMember("kim","test@spring.io",
                "12345678","kim");
        Member member2 = Member.createMember("lee","test@spring.io",
                "12345678","lee");
        //when
        memberService.join(member1);
//        IllegalStateException exception = assertThrows(IllegalStateException.class,
//                () -> memberService.join(member2));

        //then
//        assertEquals("이미 존재하는 닉네임입니다.", exception.getMessage());
    }

    @Test
    public void 회원정보수정() {
        //given
        Member member = Member.createMember("kim","spring@naver.com",
                "password123","kinopio");
        //when
        memberService.join(member);
        memberService.updateMember(member.getId(), "1234", "newname");
        Member changedMember = memberService.findOne(member.getId());
        //then
        assertThat(changedMember.getPassword()).isEqualTo("1234");
        assertThat(changedMember.getNickname()).isEqualTo("newname");
    }

    @Test
//    @Rollback(value = false)
    public void 회원가입_롤백X() {
        //given
        Member member1 = Member.createMember("a","a@naver.com",
                "password123","kuo");
        Member member2 = Member.createMember("b","b@google.com",
                "password234","mario");
        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        List<Member> members = memberService.findMembers();
//        assertThat(members.size()).isEqualTo(2);
    }

}