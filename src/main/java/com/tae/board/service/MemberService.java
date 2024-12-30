package com.tae.board.service;

import com.tae.board.domain.Member;
import com.tae.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    /**
     * 생성자 주입을 사용한다.
     */
    private final MemberRepository memberRepository;

    @Transactional
    public Long join(Member member){
        //닉네임 검증만 했음. 나중에는 모든 경우를 검증해야하나?
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByNickName(member.getNickname());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 멤버 개인 정보 조회
     * 작성한 글, 작성한 댓글, 정보
     * dto를 따로 만들어야 할 듯
     */
    public void findMemberInfo() {
        //개인정보
        //작성한 글
        //작성한 댓글
    }

}
