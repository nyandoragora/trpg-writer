package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BootyForm {

    @NotNull(message = "シナリオIDは必須です。")
    private Integer scenarioId;

    @NotBlank(message = "ダイス数を入力してください。")
    private String diceNum;

    @NotBlank(message = "内容を入力してください。")
    private String content;
}
