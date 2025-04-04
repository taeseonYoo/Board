package com.tae.board.unit.service;

import com.tae.board.MemberBuilder;
import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Member;
import com.tae.board.exception.DuplicateMemberException;
import com.tae.board.repository.CommentRepository;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import com.tae.board.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;


public class MemberServiceTest {
    private MemberService memberService;
    private final MemberRepository mockMemberRepo = Mockito.mock(MemberRepository.class);
    private final CommentRepository mockCommentRepo = Mockito.mock(CommentRepository.class);
    private final PostRepository mockPostRepo = Mockito.mock(PostRepository.class);
    private final BCryptPasswordEncoder mockBCrypt = Mockito.mock(BCryptPasswordEncoder.class);
    @BeforeEach
    public void setUp() {
        memberService = new MemberService(mockMemberRepo, mockPostRepo, mockCommentRepo, mockBCrypt);
    }

    @DisplayName("비밀번호는 수정, 닉네임은 그대로")
    @Test
    public void 회원정보_수정_1() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .password("12345678")
                .nickname("그대로")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("Before"));

        BDDMockito.given(mockBCrypt.encode(any()))
                .willReturn("After");

        //비밀번호 새로운 값, 기존 값 일치하지 않는다.
        BDDMockito.given(mockBCrypt.matches(any(), any()))
                .willReturn(false);

        //when
        memberService.updateMember(1L,"87654321","그대로");

        //then
        Member member = mockMemberRepo.findOne(1L);
        assertThat(member.getNickname()).isEqualTo("그대로");
        assertThat(member.getPassword()).isEqualTo("After");
        //then 구현 검증
        BDDMockito.then(mockMemberRepo).should(never()).findByNickName(any());
    }

    @DisplayName("비밀번호는 그대로, 닉네임은 수정")
    @Test
    public void 회원정보_수정_2() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .password("12345678")
                .nickname("Before")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("BeforePW"));
        BDDMockito.given(mockBCrypt.matches(any(), any()))
                .willReturn(true);

        //when
        memberService.updateMember(1L,"12345678","After");

        //then
        Member member = memberService.findOne(1L);
        assertThat(member.getNickname()).isEqualTo("After");
        assertThat(member.getPassword()).isEqualTo("BeforePW");
        BDDMockito.then(mockBCrypt).should(never()).encode(any());
    }

    @DisplayName("비밀번호는 수정, 닉네임도 수정")
    @Test
    public void 회원정보_수정_3() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .password("12345678")
                .nickname("Before")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("BeforePW"));
        BDDMockito.given(mockBCrypt.encode(any()))
                .willReturn("AfterPW");

        BDDMockito.given(mockBCrypt.matches(any(), any()))
                .willReturn(false);

        //when
        memberService.updateMember(1L,"87654321","After");

        //then
        Member member = memberService.findOne(1L);
        assertThat(member.getNickname()).isEqualTo("After");
        assertThat(member.getPassword()).isEqualTo("AfterPW");
    }
    @DisplayName("비밀번호 그대로, 닉네임도 그대로")
    @Test
    public void 회원정보_수정_4() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .password("12345678")
                .nickname("Before")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("BeforePW"));

        BDDMockito.given(mockBCrypt.matches(any(), any()))
                .willReturn(true);

        //when
        memberService.updateMember(any(),"12345678","Before");

        //then
        Member member = memberService.findOne(any());
        assertThat(member.getNickname()).isEqualTo("Before");
        assertThat(member.getPassword()).isEqualTo("BeforePW");
        BDDMockito.then(mockMemberRepo).should(never()).findByNickName(any());
    }

    @DisplayName("닉네임 수정 시 닉네임 중복 오류")
    @Test
    public void 회원정보_수정_닉네임_중복() {
        //given
        MemberForm memberForm = MemberBuilder.builder()
                .password("12345678")
                .nickname("Before")
                .build();

        BDDMockito.given(mockMemberRepo.findOne(any()))
                .willReturn(memberForm.toEntity("BeforePW"));

        BDDMockito.given(mockBCrypt.matches(any(), any()))
                .willReturn(true);
        BDDMockito.given(mockMemberRepo.findByNickName(any()))
                .willThrow(DuplicateMemberException.class);

        //when & then
        Assertions.assertThrows(
                DuplicateMemberException.class,
                () -> memberService.updateMember(any(), "12345678", "After"));
        //비밀번호는 변경되지 않았다.
        Member member = memberService.findOne(any());
        assertThat(member.getNickname()).isEqualTo("Before");
        assertThat(member.getPassword()).isEqualTo("BeforePW");
    }

}
