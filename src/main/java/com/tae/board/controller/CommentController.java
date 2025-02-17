package com.tae.board.controller;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Post;
import com.tae.board.dto.CommentSaveDto;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/board/post/{postId}/comment")
    public String create(@AuthenticationPrincipal MemberDetail memberDetail, @PathVariable Long postId,
                         @Valid CommentForm commentForm, BindingResult bindingResult,Model model) {

        if (bindingResult.hasErrors()) {
            Post post = postService.viewPost(postId);
            model.addAttribute("post", post);
            List<Comments> comments = post.getComments();
            model.addAttribute("comments", comments);
            return "post/postForm";
        }
        CommentSaveDto commentSaveDto = CommentSaveDto.createCommentSaveDto(commentForm.getComment(), memberDetail.getMember().getId(), postId);
        commentService.saveComment(commentSaveDto);

        return "redirect:/board/post/" + postId;
    }
}
