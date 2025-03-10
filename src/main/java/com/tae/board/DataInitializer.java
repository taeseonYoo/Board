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

@Component
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
        loadCommentsFromFile("comments.txt", n, member.getId());
    }

    @Transactional
    public int loadPostsFromFile(String filePath, Long memberId) {
        int line = 0;
        try {
            Resource resource = new ClassPathResource(filePath);
            List<String> lines;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().collect(Collectors.toList());
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
            e.printStackTrace();
            System.out.println("파일을 읽을 수 없습니다: " + filePath);
        }
        return line;
    }

    @Transactional
    public void loadCommentsFromFile(String filePath, int postCount, Long memberId) {
        try {
            Resource resource = new ClassPathResource(filePath);
            List<String> lines;

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
                lines = reader.lines().collect(Collectors.toList());
            }

            for (int i = 0; i < postCount; i++) {
                for (int j = i * 2; j < i * 2 + 2; j++) {
                    String comment = lines.get(j);
                    commentService.write((long) (i + 1), memberId, comment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("파일을 읽을 수 없습니다: " + filePath);
        }
    }
}
