package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SkillForm {

    @NotNull(message = "シナリオIDは必須です。")
    private Integer scenarioId;

    @NotBlank(message = "能力名を入力してください。")
    private String name;

    @NotBlank(message = "内容を入力してください。")
    private String content;
}
