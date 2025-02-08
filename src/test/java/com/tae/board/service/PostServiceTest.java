package com.tae.board.service;

import com.tae.board.controller.form.CommentForm;
import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.exception.UnauthorizedAccessException;
import com.tae.board.repository.PostRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    MemberService memberService;

    @Test
    public void 게시글_작성() {

        //given
        String title = "오늘의 첫 게시물";
        String content = "반갑습니다!";
        Member member = createMember("테스터", "test@spring.com", "123456", "test");

        //when
        Post post = createPost(member, title, content);
        Post savedPost = postService.findOne(post.getId());

        //then
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);

        //then-연관관계 체크
        assertThat(savedPost.getMember()).isEqualTo(member);
        Assertions.assertTrue(member.getPosts().contains(post));
    }
    @Test
    public void 조회수_증가() {

        //given
        Member member = createMember("테스터", "test@spring.com", "123456", "test");
        Post post = createPost(member, "조회 수 증가한다!", "얼마나 증가할까?");
        int target = 54321;

        //when
        for (int i = 0; i < target; i++) {
            postService.viewPost(post.getId());
        }
        Post savedPost = postService.findOne(post.getId());


        //then
        assertThat(savedPost.getViewCount()).isEqualTo(target);
    }

    @Test
    public void 게시글_수정() {
        //given
        String title = "오늘의 첫 게시물";
        String content = "반갑습니다!";
        Member member = createMember("테스터", "test@spring.com", "123456", "test");
        Post post = createPost(member, title, content);

        //when
        String modifyTitle = "두번째 게시물 입니다.";
        String modifyContent = "방가방가";
        Long updateId = postService.update(post.getId(), member.getId(), modifyTitle, modifyContent);
        Post savedPost = postService.findOne(updateId);

        //then
        assertThat(savedPost.getTitle()).isEqualTo(modifyTitle);
        assertThat(savedPost.getContent()).isEqualTo(modifyContent);
    }
    @Test
    public void 게시글_수정_실패() {
        //given
        String title = "오늘의 첫 게시물";
        String content = "반갑습니다!";
        Member member = createMember("테스터", "test@spring.com", "123456", "test");
        String modifyTitle = "두번째 게시물 입니다.";
        String modifyContent = "방가방가";
        Post post = createPost(member, title, content);

        //when
        Member fake = createMember("가짜 테스트", "sp@spring.com", "123456", "fake");


        //then
        assertThatThrownBy(() -> {
            postService.update(post.getId(), fake.getId(), modifyTitle, modifyContent);
        }).isInstanceOf(UnauthorizedAccessException.class)
                .hasMessage("게시글 삭제 권한이 없습니다.");

    }
    @Test
    public void 게시글_삭제() {
        //given
        Member member = createMember("테스터", "test@spring.com", "123456", "test");
        Post post = createPost(member, "오늘의 첫 게시물", "반갑습니다!");

        //when
        postService.deletePost(post.getId(), member.getId());
        List<Post> allPosts = postService.findPosts();

        //then
        Assertions.assertFalse(allPosts.contains(post));
        Assertions.assertFalse(member.getPosts().contains(post));

    }
    @Test
    public void 게시글_삭제_실패() {
        //given
        Member member = createMember("테스터", "test@spring.com", "123456", "test");
        Post post = createPost(member, "오늘의 첫 게시물", "반갑습니다!");
        Member fake = createMember("가짜 테스트", "sp@spring.com", "123456", "fake");


        //when & then
        assertThatThrownBy(() -> {
            postService.deletePost(post.getId(), fake.getId());
        }).isInstanceOf(UnauthorizedAccessException.class)
                .hasMessage("게시글 삭제 권한이 없습니다.");

        //then - 연관관계 체크
        Assertions.assertTrue(member.getPosts().contains(post));
        assertThat(post.getMember()).isEqualTo(member);
    }


    private Member createMember(String name, String email, String password, String nickname) {
        Member member = Member.createMember(name, email, password, nickname);
        Long memberId = memberService.join(member);
        return memberService.findOne(memberId);
    }

    private Post createPost(Member member, String title, String content) {
        Long postId = postService.write(member.getId(), title, content);
        return postService.findOne(postId);
    }

}