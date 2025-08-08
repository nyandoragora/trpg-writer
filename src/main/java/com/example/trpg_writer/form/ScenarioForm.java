package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

@Data
public class ScenarioForm {
    private Integer id;

    @NotBlank(message = "タイトルを入力してください。")
    @Size(max = 100, message = "タイトルは100文字以内で入力してください。")
    private String title;

    @Size(max = 65535, message = "概要は65535文字以内で入力してください。")
    private String introduction;
}