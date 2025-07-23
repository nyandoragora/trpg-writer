package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class InfoForm {
    private Integer id;

    private Integer scenarioId;

    @NotBlank(message = "情報名を入力してください。")
    @Size(max = 255, message = "情報名は255文字以内で入力してください。")
    private String name;

    @Size(max = 65535, message = "情報内容は65535文字以内で入力してください。")
    private String content;
}
