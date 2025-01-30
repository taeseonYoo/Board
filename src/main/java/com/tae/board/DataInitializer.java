package com.tae.board;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
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


        CommentForm commentForm = new CommentForm();
        commentForm.setComment("아 그거 대박이에요 ㅋㅋㅋㅋ");
        commentForm.setMemberId(member.getId());
        commentForm.setPostId(postId);
        commentService.saveComment(commentForm);

        CommentForm commentForm2 = new CommentForm();
        commentForm2.setComment("맞아요!");
        commentForm2.setMemberId(member.getId());
        commentForm2.setPostId(postId);
        commentService.saveComment(commentForm2);

    }


}
