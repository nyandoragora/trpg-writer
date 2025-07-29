package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PartForm {

    @NotNull(message = "シナリオIDは必須です。")
    private Integer scenarioId;

    @NotBlank(message = "部位名を入力してください。")
    private String name;

    @NotNull(message = "命中力を入力してください。")
    private Integer hit;

    @NotBlank(message = "打点を入力してください。")
    private String damage;

    @NotNull(message = "回避力を入力してください。")
    private Integer evasion;

    @NotNull(message = "防護点を入力してください。")
    private Integer protection;

    @NotNull(message = "HPを入力してください。")
    private Integer lifePoint;

    @NotNull(message = "MPを入力してください。")
    private Integer magicPoint;
}
