package com.tae.board.controller;

import com.tae.board.domain.Member;
import com.tae.board.security.MemberDetail;
import com.tae.board.service.MemberService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    MemberService memberService;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Test
    @WithAnonymousUser
    void 로그인() throws Exception {

        //회원 가입
        String password = "12345678";
        Member member = Member.createMember("테스터","test@spring.com",
                bCryptPasswordEncoder.encode(password),
                "test");
        Long id = memberService.join(member);
        Member findMember = memberService.findOne(id);

        //Test
        mockMvc.perform(post("/auth/login")
                        .param("email", findMember.getEmail())
                        .param("password", password))
                .andExpect(authenticated())
                .andExpect(status().is3xxRedirection()) // 성공 시 리다이렉트 확인
                .andExpect(redirectedUrl("/board"));
    }

    @Test
    @WithAnonymousUser
    void 로그인_실패() throws Exception {
        //회원 가입
        String password = "12345678";
        Member member = Member.createMember("테스터","test@spring.com",
                bCryptPasswordEncoder.encode(password),
                "test");
        Long id = memberService.join(member);
        Member findMember = memberService.findOne(id);

        mockMvc.perform(post("/auth/login")
                        .param("email", findMember.getEmail())
                        .param("password", "wrong_password"))
                .andExpect(status().is3xxRedirection())  // 리다이렉트 상태 코드 확인
                .andExpect(redirectedUrl("/auth/login?error=true"));
    }


    @Test
    @WithAnonymousUser
    void 회원가입() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .param("name", "코코")
                        .param("email", "test@spring.com")
                        .param("password", "12345678")
                        .param("nickname", "nick"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }
    @Test
    @WithAnonymousUser
    void 회원가입_실패() throws Exception{
        mockMvc.perform(post("/auth/register")
                        .param("name", "")
                        .param("email", "12345678")
                        .param("password", "test@example.com")
                        .param("nickname", "nick"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberJoin"))
                .andExpect(model().attributeHasFieldErrors("memberForm", "name"));
    }
    @Test
    void 마이페이지() throws Exception {

        String password = "12345678";
        Member member = Member.createMember("테스터","test@spring.com",
                bCryptPasswordEncoder.encode(password),
                "test");
        memberService.join(member);

        mockMvc.perform(get("/auth/mypage")
                        .with(SecurityMockMvcRequestPostProcessors.user(new MemberDetail(member))))
                .andExpect(status().isOk())
                .andExpect(view().name("member/myPageForm"));

    }

    /**
     * @WithMockUser가 없으면 인증되지 않은 상태 이므로
     * 실패함
     */
    @Test
    @WithMockUser
    void 글쓰기_페이지() throws Exception {
        mockMvc.perform(get("/board/write"))
                .andExpect(status().isOk())
                .andExpect(view().name("post/createPostForm"));
    }

    @Test
    @WithMockUser
    void 로그아웃() throws Exception {
        mockMvc.perform(get("/auth/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    @WithAnonymousUser
    void 회원가입_페이지() throws Exception {
        mockMvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/memberJoin"));
    }
    @Test
    @WithAnonymousUser
    void 회원가입_페이지_실패() throws Exception {
        mockMvc.perform(get("/auth/register1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }
    @Test
    @WithAnonymousUser
    void 로그인_페이지() throws Exception {
        mockMvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("member/loginForm"));
    }
    @Test
    @WithAnonymousUser
    void 로그인_페이지_실패() throws Exception {
        mockMvc.perform(get("/auth/login1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/auth/login"));
    }


}

