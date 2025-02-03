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


        CommentForm commentForm = CommentForm.from("아 그거 대박이에요 ㅋㅋㅋㅋ", member.getId(), postId);
        commentService.saveComment(commentForm);

        CommentForm commentForm2 = CommentForm.from("맞아요!", member.getId(),postId);
        commentService.saveComment(commentForm2);

    }


}
