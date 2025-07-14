package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class SignupForm {
    @NotBlank(message = "ユーザー名を入力してください。")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください。")
    private String name;

    @NotBlank(message = "メールアドレスを入力してください。")
    private String email;

    @NotBlank(message = "パスワードを入力してください。")
    @Size(min = 8, max = 50, message = "パスワードは8文字以上50文字以内で入力してください。")
    private String password;

    @NotBlank(message = "パスワード（確認用）を入力してください。")
    private String passwordConfirmation;
}
