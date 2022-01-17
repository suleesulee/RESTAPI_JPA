package com.example.jpa.user.model;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserPasswordResetInput {

    @Email (message = "이메일 형식이 아닙니다.")
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String userName;
}
