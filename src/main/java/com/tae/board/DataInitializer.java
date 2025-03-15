package com.tae.board;

import com.tae.board.domain.Member;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

//@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostService postService;
    private final CommentService commentService;

    @PostConstruct
    public void init() {
        String password = bCryptPasswordEncoder.encode("12345678");
        Member member = Member.createMember("홍길동", "kino@spring.com", password, "키노");
        memberService.join(member);

        int n = loadPostsFromFile("posts.txt", member.getId());
        loadCommentsFromFile("comments.txt",n, member.getId());
//        loadCommentsForTest(1L,"comments.txt");
    }

    public int loadPostsFromFile(String filePath, Long memberId) {
        int line = 0;
        try {
            Resource resource = new ClassPathResource(filePath);
            List<String> lines;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().toList();
            }

            line = lines.size() / 2;
            for (int i = 0; i < lines.size(); i += 2) {
                if (i + 1 < lines.size()) {
                    String title = lines.get(i);
                    String content = lines.get(i + 1);
                    postService.write(memberId, title, content);
                }
            }
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다: " + filePath);
        }
        return line;
    }

    public void loadCommentsFromFile(String filePath, int postCount, Long memberId) {
        try {
            Resource resource = new ClassPathResource(filePath);
            List<String> lines;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().toList();
            }

            for (int i = 0; i < postCount; i++) {
                for (int j = i * 2; j < i * 2 + 2; j++) {
                    String comment = lines.get(j);
                    commentService.write((long) (i + 1), memberId, comment);
                }
            }
        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다: " + filePath);
        }
    }

    public void loadCommentsForTest(Long postId,String filePath) {
        String password = bCryptPasswordEncoder.encode("12345678");
        for (int i = 0; i < 100; i++) {
            Member member1 = Member.createMember(i+"", i+"@spring.com", password, i+"");
            memberService.join(member1);
        }
        try {
            Resource resource = new ClassPathResource(filePath);
            List<String> lines;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().toList();
            }

            for (int j = 1; j < lines.size(); j++) {
                String comment = lines.get(j);
                commentService.write(postId, (long)j, comment);
            }

        } catch (IOException e) {
            System.out.println("파일을 읽을 수 없습니다: " + filePath);
        }
    }
}
