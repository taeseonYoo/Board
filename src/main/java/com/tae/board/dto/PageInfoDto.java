package com.tae.board.dto;

import com.tae.board.domain.Post;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageInfoDto {
    private int startPage;
    private int endPage;
    private int currentPage;
    private int totalPages;
    private Page<Post> posts;

    protected PageInfoDto() {
    }
    private PageInfoDto(int startPage, int endPage, int currentPage, int totalPages, Page<Post> posts) {
        this.startPage = startPage;
        this.endPage = endPage;
        this.currentPage = currentPage;
        this.totalPages = totalPages;
        this.posts = posts;
    }

    public static PageInfoDto createPageInfo(int startPage, int endPage, int currentPage, int totalPages, Page<Post> posts) {
        return new PageInfoDto(startPage, endPage, currentPage, totalPages, posts);
    }

}
