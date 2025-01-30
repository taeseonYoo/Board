package com.tae.board.service;

import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
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
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");

        //when
        Long savedPostId = postService.write(member.getId(),"JPA","JPA에 대한 학습중 입니다.");
        Post post = postService.findOne(savedPostId);
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

        //when
        Long savedPostId = postService.write(member.getId(),"JPA", "JPA에 대한 학습중 입니다.");
        Post post = postService.findOne(savedPostId);
        Post findPost = postService.findOne(savedPostId);
        //then
        assertThat(post.getViewCount()).isEqualTo(0);
    }

    @Test
    public void 조회수_증가() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");


        //when
        Long savedPostId = postService.write(member.getId(),"JPA", "JPA에 대한 학습중 입니다.");

        postService.viewPost(savedPostId);
        postService.viewPost(savedPostId);

        //then
        assertThat(postService.findOne(savedPostId).getViewCount()).isEqualTo(2);
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
    public void 게시글_수정1() {
        //given
        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Long savedId = postService.write(member.getId(),"JPA", "JPA에 대한 학습중 입니다.");

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

    @Test
    public void 게시글_수정() {
        //given
        String title = "변경 전";
        String content = "변경 전 게시글 내용입니다";
        String updateTitle = "변경 후";
        String updateContent = "변경 후 게시글 내용입니다";

        Member member = createMember("tae", "kino@spring.com", "123456", "kino");
        Post post = createPost(member, title, content, member.getNickname());
        //then
        Long updateId = postService.update(post.getId(), updateTitle, updateContent);
        Post updatedPost = postService.findOne(updateId);

        //when
        assertThat(updatedPost.getTitle()).isEqualTo(updateTitle);
        assertThat(updatedPost.getContent()).isEqualTo(updateContent);

        assertThat(updatedPost.getTitle()).isNotEqualTo(title);
        assertThat(updatedPost.getContent()).isNotEqualTo(content);
    }


    private Member createMember(String name, String email, String password, String nickname) {
        Member member = Member.createMember(name, email, password, nickname);
        memberService.join(member);
        return member;
    }

    private Post createPost(Member member, String title, String content, String nickname) {

        Long write = postService.write(member.getId(), title, content);
        return postService.findOne(write);
    }

}