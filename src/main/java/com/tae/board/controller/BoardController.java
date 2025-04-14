package com.tae.board.controller;

import com.tae.board.controller.form.CommentEditForm;
import com.tae.board.controller.form.CommentForm;
import com.tae.board.controller.form.PostForm;
import com.tae.board.domain.Comment;
import com.tae.board.domain.Post;
import com.tae.board.dto.PageInfoDto;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.CommentService;
import com.tae.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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
    private final CommentService commentService;

    @GetMapping({"/", "/board"})
    public String home(@AuthenticationPrincipal MemberDetail memberDetail, Model model,
                       @PageableDefault(page = 0,size = 10) Pageable pageable) {

        if (memberDetail != null) {
            model.addAttribute("nickname", memberDetail.getNickname());
        }

        PageInfoDto pageInfo = postService.findPagePosts(pageable);


        model.addAttribute("pageInfo", pageInfo);

        return "board";
    }

    //게시글 생성 페이지
    @GetMapping("/board/write")
    public String createForm(@ModelAttribute PostForm postForm) {

        return "post/createPostForm";
    }

    //게시글 조회 페이지
    @GetMapping("/board/post/{postId}")
    public String post(@PathVariable Long postId,
                       @ModelAttribute CommentForm commentForm,
                       @ModelAttribute CommentEditForm commentEditForm,
                       Model model) {

        Post post = postService.viewPost(postId);
        model.addAttribute("post", post);

        List<Comment> comments = commentService.findByPostIdOrderByCreatedDateDesc(postId);
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
    public String update(@PathVariable Long postId, @AuthenticationPrincipal MemberDetail memberDetail,
                         @Valid PostForm postForm, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "post/editPostForm";
        }
        Long updateId = postService.update(postId,memberDetail.getMember().getId(),postForm.getTitle(), postForm.getContent());

        return "redirect:/board/post/" + updateId;
    }


    @DeleteMapping("/board/post/{postId}/delete")
    public String delete(@PathVariable Long postId, @AuthenticationPrincipal MemberDetail memberDetail) {

        postService.deletePost(postId, memberDetail.getMember().getId());

        return "redirect:/board";
    }

}
