package com.tae.board;

import com.tae.board.domain.Member;
import com.tae.board.domain.Post;
import com.tae.board.service.CommentService;
import com.tae.board.service.MemberService;
import com.tae.board.service.PostService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    private final MemberService memberService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final PostService postService;
    private final CommentService commentService;

    @PostConstruct
    public void init(){

        String password = bCryptPasswordEncoder.encode("12345678");
        Member member = Member.createMember("홍길동", "kino@spring.com",
                password, "키노");
        memberService.join(member);

        int n = loadPostsFromFile("src/main/resources/posts.txt", member.getId());
        loadCommentsFormFile("src/main/resources/comments.txt", n, member.getId());

    }


    @Transactional
    public int loadPostsFromFile(String filePath,Long memberId) {
        int line = 0;
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            line = lines.size()/2;
            for (int i = 0; i < lines.size(); i += 2) {
                if (i + 1 < lines.size()) {
                    String title = lines.get(i);
                    String content = lines.get(i + 1);
                    postService.write(memberId,title,content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }
    @Transactional
    public void loadCommentsFormFile(String filePath,int postCount,Long memberId) {
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));
            for (int i = 0; i < postCount; i++) {
                for (int j = i*2; j < i*2 +2; j++) {
                    String comment = lines.get(j);
                    commentService.write((long)(i+1), memberId, comment);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}