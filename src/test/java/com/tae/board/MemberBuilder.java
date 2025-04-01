package com.tae.board;

import com.tae.board.controller.form.MemberForm;

public class MemberBuilder {
    private String name = "테스터";
    private String nickname = "테테";
    private String password = "12345678";
    private String email = "test@spring.io";
    public static MemberBuilder builder() {
        return new MemberBuilder();
    }
    public MemberBuilder name(String name) {
        this.name = name;
        return this;
    }
    public MemberBuilder nickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public MemberBuilder password(String password) {
        this.password = password;
        return this;
    }
    public MemberBuilder email(String email) {
        this.email = email;
        return this;
    }

    public MemberForm build() {
        return new MemberForm(name, email, password, nickname);
    }
}
