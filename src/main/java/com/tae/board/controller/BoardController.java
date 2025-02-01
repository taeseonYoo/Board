package com.tae.board.controller;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.controller.form.PostForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Post;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;

    //메인 화면
    @GetMapping({"/", "/board"})
    public String home(@AuthenticationPrincipal MemberDetail memberDetail,Model model) {
        List<Post> posts = postService.findPosts();

        if (memberDetail != null) {
            model.addAttribute("nickname", memberDetail.getNickname());
        }

        model.addAttribute("posts", posts);
        return "board";
    }

    //게시글 생성 페이지
    @GetMapping("/board/write")
    public String createForm(@ModelAttribute PostForm postForm) {

        return "post/createPostForm";
    }

    //게시글 조회 페이지
    @GetMapping("/board/post/{postId}")
    public String post(@PathVariable Long postId, @ModelAttribute CommentForm commentForm, Model model) {

        Post post = postService.findOne(postId);
        model.addAttribute("post",post);

        List<Comments> comments = post.getComments();
        model.addAttribute("comments", comments);

        return "post/postForm";
    }

    @GetMapping("/board/post/{postId}/edit")
    public String edit(@PathVariable Long postId, Model model) {

        Post post = postService.findOne(postId);
        PostForm postForm = PostForm.from(post);

        model.addAttribute("postForm", postForm);

        return "post/editPostForm";
    }

    @PostMapping("/board/write")
    public String create(@AuthenticationPrincipal MemberDetail memberDetail, @Valid PostForm postForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "post/createPostForm";
        }
        postService.write(memberDetail.getMember().getId(), postForm.getTitle(), postForm.getContent());

        /**
         * 작성하고 게시글 화면으로 가는 게 좋겠다.
         */
        return "redirect:/board";
    }

}
