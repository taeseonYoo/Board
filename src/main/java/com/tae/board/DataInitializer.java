package com.tae.board;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Member;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostService postService;
    private final CommentService commentService;

    @PostConstruct
    public void init(){

        String password = bCryptPasswordEncoder.encode("12345678");
        Member member = Member.createMember("홍길동", "kino@spring.com",
                password, "키노");
        memberService.join(member);

        Long postId = postService.write(member.getId(), "님들 그거 보셨음?", "내용은 뭔가요");

        for (int i = 1; i < 101; i++) {
            postService.write(member.getId(), "게시글" + i, "내용" + i);
        }


        commentService.saveComment(postId, member.getId(), "아 그거 대박이예요 ㅋㅋㅋㅋ");

        commentService.saveComment(postId, member.getId(), "맞아요!");

    }


}
