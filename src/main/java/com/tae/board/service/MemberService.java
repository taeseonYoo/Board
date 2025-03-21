package com.tae.board.service;

import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.dto.MemberInfoDto;
import com.tae.board.repository.CommentRepository;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;


    @Transactional
    public Long join(Member member){
        //닉네임 검증만 했음.
        validateDuplicateMember2(member);
        memberRepository.save(member);
        return member.getId();
    }

    @Transactional
    public void updateMember(Long memberId, String password, String nickname) {
        Member member = memberRepository.findOne(memberId);
        if (nickname.equals(member.getNickname())) {
            validateDuplicateMember(member);
        }
        member.update(password, nickname);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }



    @Transactional
    public MemberInfoDto getMemberInfo(String email) {
        Member findByEmail = memberRepository.findByEmail(email);
        List<Post> findPosts = postRepository.findAllByMember(findByEmail.getId());
        List<Comments> findComments = commentRepository.findByMember(findByEmail.getId());

        return MemberInfoDto.createMemberInfo(findByEmail.getName(),
                findByEmail.getEmail(),
                findByEmail.getCreatedDate(),
                findByEmail.getNickname(),
                findPosts,
                findComments);
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByNickName(member.getNickname());
        List<Member> byEmail2 = memberRepository.findByEmail2(member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
        if (!byEmail2.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 이메일");
        }
    }
    private void validateDuplicateMember1(Member member) {
        List<Member> findMembers = memberRepository.findByNickNameOrEmail(member.getNickname(),member.getEmail());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
    private void validateDuplicateMember2(Member member) {
        boolean exists = memberRepository.existsByNickNameOrEmail(member.getNickname(), member.getEmail());
        if (exists) {
            throw new IllegalStateException("이미 존재하는 닉네임입니다.");
        }
    }
}
