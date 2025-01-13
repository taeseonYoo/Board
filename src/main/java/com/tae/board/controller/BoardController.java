package com.tae.board.controller;

import com.tae.board.controller.form.PostForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final MemberService memberService;

    @GetMapping({"/", "/board"})
    public String home(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "board";
    }

    @GetMapping("/board/write")
    public String createForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "post/createPostForm";
    }

    @PostMapping("/board/post")
    public String create(PostForm postForm) {

        Member member = Member.createMember("tae","tae@naver,com","1234","kino");
        memberService.join(member);
        Post post = Post.createPost(member, postForm.getTitle(), postForm.getContent(), member.getNickname());

        postService.savePost(post);
        return "redirect:/board";
    }
}
