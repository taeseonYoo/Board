package com.tae.board.controller;

import com.tae.board.controller.form.CommentEditForm;
import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Post;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.CommentService;
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
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    //댓글 수정 폼 가져오기
    @GetMapping("/board/post/{postId}/comment/{commentId}")
    public String editForm(@PathVariable Long postId,@PathVariable Long commentId,
                           @ModelAttribute CommentForm commentForm,
                           Model model){

        Comments comment = commentService.findById(commentId);

        loadPostDetails(model, postId);
        model.addAttribute("commentEditForm", CommentEditForm.create(comment.getComment()));

        model.addAttribute("targetId",commentId);

        return "post/postForm";
    }

    //댓글 작성
    @PostMapping("/board/post/{postId}/comment")
    public String create(@AuthenticationPrincipal MemberDetail memberDetail, @PathVariable Long postId,
                         CommentEditForm commentEditForm,
                         @Valid CommentForm commentForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            loadPostDetails(model, postId);
            return "post/postForm";
        }

        commentService.write(postId,memberDetail.getMember().getId(), commentForm.getComment());

        return "redirect:/board/post/" + postId;
    }

    @DeleteMapping("/board/post/{postId}/comment/{commentId}")
    public String delete(@PathVariable Long postId, @PathVariable Long commentId,
                         @AuthenticationPrincipal MemberDetail memberDetail) {
        commentService.delete(commentId, postId, memberDetail.getMember().getId());

        return "redirect:/board/post/" + postId;
    }

    @PostMapping("/board/post/{postId}/comment/{commentId}")
    public String update(@PathVariable Long postId, @PathVariable Long commentId,
                         @AuthenticationPrincipal MemberDetail memberDetail,
                         CommentForm commentForm,
                         @Valid CommentEditForm commentEditForm,
                         BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            loadPostDetails(model, postId);
            model.addAttribute("targetId",commentId);
            return "post/postForm";
        }

        commentService.update(postId,commentId,memberDetail.getMember().getId(),commentEditForm);

        return "redirect:/board/post/" + postId;
    }


    private void loadPostDetails(Model model, Long postId) {
        Post post = postService.viewPost(postId);
        model.addAttribute("post", post);
        List<Comments> comments = commentService.findAllByPostOrderByCreateDate(postId);
        model.addAttribute("comments", comments);
    }

}
