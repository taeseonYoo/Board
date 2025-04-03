package com.tae.board.integration.service;

import com.tae.board.MemberBuilder;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.dto.MemberInfoDto;
import com.tae.board.exception.DuplicateMemberException;
import com.tae.board.service.MemberService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired
    MemberService memberService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Test
    public void 회원가입_성공(){
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .name("테스터")
                .nickname("테테")
                .email("test@spring.io")
                .password("12345678")
                .build();
        //when
        Long savedId = memberService.join(memberForm);

        //then
        Member savedMember = memberService.findOne(savedId);
        assertThat(savedMember).isNotNull();
        assertThat(savedMember.getName()).isEqualTo("테스터");
        assertThat(savedMember.getNickname()).isEqualTo("테테");
        assertThat(savedMember.getEmail()).isEqualTo("test@spring.io");
        assertThat(bCryptPasswordEncoder.matches("12345678", savedMember.getPassword())).isTrue();
    }

    @Test
    public void 중복_닉네임_회원가입_실패(){
        //given
        MemberForm member1 = MemberBuilder.builder()
                .nickname("동일 닉네임")
                .email("abc@spring.io")
                .build();
        MemberForm member2 = MemberBuilder.builder()
                .nickname("동일 닉네임")
                .email("cba@spring.io")
                .build();
        Long memberId1 = memberService.join(member1);

        //when & then
        assertThrows(DuplicateMemberException.class,
                () -> memberService.join(member2)
        );

        //then
        Member findMember = memberService.findOne(memberId1);
        assertThat(findMember).isNotNull();
    }

    @Test
    public void 중복_이메일_회원가입_실패() {
        //given
        MemberForm member1 = MemberBuilder.builder()
                .nickname("닉네임1")
                .email("duplicate@spring.io")
                .build();
        MemberForm member2 = MemberBuilder.builder()
                .nickname("닉네임2")
                .email("duplicate@spring.io")
                .build();
        Long memberId1 = memberService.join(member1);

        //when & then
        assertThrows(DuplicateMemberException.class,
                () -> memberService.join(member2));

        //then
        Member findMember = memberService.findOne(memberId1);
        assertThat(findMember).isNotNull();
    }

    @Test
    public void 회원_닉네임_수정() {
        //given
        MemberForm member = MemberBuilder.builder()
                .nickname("before")
                .password("12345678")
                .email("abc@spring.io")
                .build();
        Long memberId = memberService.join(member);
        Member findMember = memberService.findOne(memberId);

        //when
        memberService.updateMember(findMember.getId(), "12345678", "after");

        //then
        Member changedMember = memberService.findOne(findMember.getId());
        assertThat(changedMember.getNickname()).isEqualTo("after");
    }

    @Test
    public void 회원_비밀번호_수정() {
        //given
        MemberForm member = MemberBuilder.builder()
                .nickname("고정")
                .password("12345678")
                .email("abc@spring.io")
                .build();
        Long memberId = memberService.join(member);
        Member findMember = memberService.findOne(memberId);

        //when
        memberService.updateMember(findMember.getId(), "87654321", "고정");

        //then
        Member changedMember = memberService.findOne(findMember.getId());
        assertThat(bCryptPasswordEncoder.matches("87654321", changedMember.getPassword())).isTrue();
    }


}