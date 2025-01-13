package com.tae.board.controller.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {

    //null 이거나 빈 값일 경우 유효하지 않다.
    @NotEmpty(message = "{member.name.empty}")
    @Size(min = 2,max = 15,message = "{member.name.size}")
    private String name;

    @NotEmpty(message = "{member.password.empty}")
    @Size(min=8,max = 20,message = "{member.password.size}")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "{member.password.format}")
    private String password;

    @NotEmpty(message = "{member.password.empty}")
    @Email(message = "{member.email.format}")
    private String email;

    @NotEmpty(message = "{member.nickname.empty}")
    private String nickname;
}
