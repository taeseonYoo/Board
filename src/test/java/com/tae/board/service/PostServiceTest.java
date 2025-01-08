package com.tae.board.service;

import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.repository.MemberRepository;
import com.tae.board.repository.PostRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

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
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member,"JPA","JPA에 대한 학습중 입니다.",member.getNickname());

        //when
        Long savedPostId = postService.savePost(post);
        Post findPost = postService.findOne(savedPostId);

        //then
        Assertions.assertEquals(post.getContent(),findPost.getContent(),"작성한 게시글의 내용이 일치하는 지 확인");
        Assertions.assertEquals(post.getTitle(),findPost.getTitle(),"작성한 게시글의 제목이 일치하는 지 확인");
        Assertions.assertEquals(post.getWriter(), findPost.getWriter(), "작성자가 일치하는 지 확인");
    }

    @Test
    public void 조회수_초기화_확인() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member, "JPA", "JPA에 대한 학습중 입니다.", member.getNickname());

        //when
        Long savedPostId = postService.savePost(post);
        Post findPost = postService.findOne(savedPostId);
        //then
        assertThat(post.getViewCount()).isEqualTo(0);
    }

    @Test
    public void 조회수_증가() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member, "JPA", "JPA에 대한 학습중 입니다.", member.getNickname());

        //when
        Long savedPostId = postService.savePost(post);

        postService.viewPost(savedPostId);
        postService.viewPost(savedPostId);

        //then
        assertThat(postService.findOne(savedPostId).getViewCount()).isEqualTo(2);
    }

    @Test
    public void 조회수_검색_예외() {
        //given
        Long notSavedId = 5L;

        //when then
        assertThrows(PostNotFoundException.class,
                () -> postService.viewPost(notSavedId));
    }

    @Test
    public void 모든_게시글_검색() {
        //given
        Member member1 = createMember("tae", "kino@spring.com", "123456", "kino");
        createPost(member1, "JPA", "JPA에 대한 학습중 입니다.", member1.getNickname());
        createPost(member1, "JPA2", "JPA2에 대한 학습중 입니다.", member1.getNickname());
        createPost(member1, "JPA3", "JPA2에 대한 학습중 입니다.", member1.getNickname());



        Member member2 = createMember("mario", "mario@spring.com", "987654", "mario");
        createPost(member2, "점심 메뉴 추천!", "점심에 떡볶이 먹으렴", member2.getNickname());
        createPost(member2, "저녁 메뉴 추천!", "저녁엔 치킨 먹으렴", member2.getNickname());

        //when
        int num = 0;
        List<Member> members = memberService.findMembers();
        for (Member member : members) {
            num += postService.findPostsByMember(member.getId()).size();
        }

        //then
        assertThat(postService.findPosts().size()).isEqualTo(num);
    }

    @Test
    public void 회원별_게시글_검색() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post1 = createPost(member, "JPA", "JPA에 대한 학습중 입니다.", member.getNickname());
        Post post2 = createPost(member, "JPA2", "JPA2에 대한 학습중 입니다.", member.getNickname());
        Post post3 = createPost(member, "JPA3", "JPA2에 대한 학습중 입니다.", member.getNickname());

        //when
        List<Post> findMembers = postService.findPostsByMember(member.getId());
        //then
        assertThat(findMembers.size()).isEqualTo(3);
    }

    @Test
    public void 게시글_수정() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member, "JPA", "JPA에 대한 학습중 입니다.", member.getNickname());
        Long savedId = postService.savePost(post);

        //when
        postService.update(savedId, "변경", "내용을 변경합니다.");

        //then
        assertThat(postService.findOne(savedId).getTitle()).isEqualTo("변경");
    }


    @Test
    public void 게시글_삭제() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member, "JPA", "JPA에 대한 학습중 입니다.", member.getNickname());

        //when
        postService.deletePost(post.getId());

        //then
        assertThat(postService.findPostsByMember(member.getId()).size()).isEqualTo(0);
    }


    private Member createMember(String name, String email, String password, String nickname) {
        Member member = Member.createMember(name, email, password, nickname);
        memberService.join(member);
        return member;
    }

    private Post createPost(Member member, String title, String content, String nickname) {
        Post post = Post.createPost(member, title, content, nickname);
        postService.savePost(post);
        return post;
    }

}