package com.tae.board.service;

import com.tae.board.controller.form.MemberForm;
import com.tae.board.domain.Comment;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.dto.MemberInfoDto;
import com.tae.board.exception.DuplicateMemberException;
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
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public Long join(MemberForm memberForm){
        //닉네임과 이메일에 대한 중복 검증
        validateDuplicateNickname(memberForm.getNickname());
        validateDuplicateEmail(memberForm.getEmail());
        //비밀번호 암호화 및 회원 생성
        Member member = memberForm.toEntity(bCryptPasswordEncoder.encode(memberForm.getPassword()));
        memberRepository.save(member);

        return member.getId();
    }

    @Transactional
    public void updateMember(Long memberId, String password, String nickname) {

        Member member = memberRepository.findOne(memberId);

        boolean isNickNameUnchanged = nickname.equals(member.getNickname());
        boolean isPasswordUnchanged = bCryptPasswordEncoder.matches(password,member.getPassword());

        if (!isNickNameUnchanged) {
            validateDuplicateNickname(nickname);
            member.updateNickName(nickname);
        }
        if (!isPasswordUnchanged) {
            member.updatePassword(bCryptPasswordEncoder.encode(password));
        }

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
        List<Comment> findComments = commentRepository.findByMember(findByEmail.getId());

        return MemberInfoDto.createMemberInfo(findByEmail.getName(),
                findByEmail.getEmail(),
                findByEmail.getCreatedDate(),
                findByEmail.getNickname(),
                findPosts,
                findComments);
    }

    private void validateDuplicateNickname(String nickname) {
        List<Member> members = memberRepository.findByNickName(nickname);
        if (!members.isEmpty()) {
            throw new DuplicateMemberException("이미 존재하는 닉네임입니다.");
        }
    }

    private void validateDuplicateEmail(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            throw new DuplicateMemberException("이미 존재하는 이메일입니다.");
        }
    }
}
