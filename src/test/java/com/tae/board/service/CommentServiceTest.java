package com.tae.board.service;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Comments;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.dto.CommentSaveDto;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class CommentServiceTest {
    @Autowired
    private CommentService commentService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private PostService postService;
    @Autowired
    EntityManager em;


    @Test
    public void 댓글_생성() {

        //given
        Member member = createMember("SOO", "SOO@spring.jpa", "1234", "SOON");
        Post post = createPost(member, "제목입니다.", "내용은 무엇으로 할까요?", member.getNickname());
        Long commentId = createComments("내용은 SpringJPA에 대해서 해주세요", member, post);

        //when

        List<Comments> allByMember = commentService.findAllByMember(member.getId());
        List<Comments> allByPost = commentService.findAllByPost(post.getId());

        Comments byId = commentService.findById(commentId);
        System.out.println(byId.getMember().getId());
        //then

        assertThat(allByMember.size()).isEqualTo(1);
        assertThat(allByPost.size()).isEqualTo(1);
//
        assertThat(byId.getMember().getId()).isEqualTo(member.getId());
        assertThat(byId.getPost().getId()).isEqualTo(post.getId());
        assertThat(byId.getComment()).isEqualTo("내용은 SpringJPA에 대해서 해주세요");

    }

    @Test
    public void 댓글_삭제() {
        //given
        Member member = createMember("SOO", "SOO@spring.jpa", "1234", "SOON");
        Post post = createPost(member, "제목입니다.", "내용은 무엇으로 할까요?", member.getNickname());
        Long commentId = createComments("내용은 SpringJPA에 대해서 해주세요", member, post);
        Long commentId2 = createComments("ㅋㅋㅋ SpringJPA에 대해서 해주세요", member, post);

        //when
        Comments byId = commentService.findById(commentId);
        byId.removePost();
        commentService.deleteComments(commentId, post.getId(), member.getId());
        em.flush();
        em.clear();
        List<Comments> allByPost = commentService.findAllByPost(post.getId());
        List<Comments> allByMember = commentService.findAllByMember(member.getId());

        //then
        assertThat(allByMember.size()).isEqualTo(1);
        assertThat(allByPost.size()).isEqualTo(1);

    }

    @Test
    public void 다른회원_댓글(){
        //given
        Member member = createMember("SOO", "SOO@spring.jpa", "1234", "SOON");
        Member member1 = createMember("KIM", "KIM@spring.jpa", "1234", "KIMS");
        Post post = createPost(member, "제목입니다.", "내용은 무엇으로 할까요?", member.getNickname());
        Long commentId = createComments("내용은 SpringJPA에 대해서 해주세요", member, post);
        Long commentId2 = createComments("저는 반대입니다.", member1, post);

        //when
        commentService.deleteComments(commentId, post.getId(), member.getId());


        //then
        assertThat(commentService.findAllByPost(post.getId()).size()).isEqualTo(1);
    }

    @Test
    public void 댓글_수정() {
        //given
        Member member = createMember("SOO", "SOO@spring.jpa", "1234", "SOON");
        Post post = createPost(member, "제목입니다.", "내용은 무엇으로 할까요?", member.getNickname());
        Long commentId = createComments("내용은 SpringJPA에 대해서 해주세요", member, post);

        //when
//        CommentForm commentForm = new CommentForm();

//        commentForm.setMemberId(member.getId());
//        commentForm.setPostId(post.getId());
//        commentForm.setComment("Spring 말고 다른걸로 합시다.");
//        commentService.update(commentId, commentForm);
        //then

        assertThat(commentService.findById(commentId).getComment()).isEqualTo("Spring 말고 다른걸로 합시다.");

    }



    private Long createComments(String comment, Member member,Post post) {

        CommentSaveDto commentSaveDto = CommentSaveDto.createCommentSaveDto(comment, member.getId(), post.getId());

        return commentService.saveComment(commentSaveDto);
    }

    private Member createMember(String name, String email, String password, String nickname) {
        Member member = Member.createMember(name, email, password, nickname);
        memberService.join(member);
        return member;
    }

    private Post createPost(Member member, String title, String content, String nickname) {

        Long postId = postService.write(member.getId(), title, content);
        return postService.findOne(postId);
    }

}