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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;

    //메인 화면
    @GetMapping({"/", "/board"})
    public String home(@AuthenticationPrincipal MemberDetail memberDetail, Model model) {
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
        model.addAttribute("post", post);

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
        Long postId = postService.write(memberDetail.getMember().getId(), postForm.getTitle(), postForm.getContent());

        return "redirect:/board/post/" + postId;
    }

    //게시글 수정
    @PostMapping("/board/post/{postId}")
    public String update(@PathVariable Long postId,
                         @Valid PostForm postForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "post/editPostForm";
        }
        Long updateId = postService.update(postId, postForm.getTitle(), postForm.getContent());

        return "redirect:/board/post/" + updateId;
    }


    @PostMapping("/board/post/{postId}/delete")
    public String delete(@AuthenticationPrincipal MemberDetail memberDetail, @PathVariable Long postId) {
        //권한 확인

        //삭제
        postService.deletePost(postId);

        return "redirect:/board";
    }
}
