package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SceneForm {
    private Integer id;

    @NotBlank(message = "シーンタイトルを入力してください。")
    @Size(max = 255, message = "シーンタイトルは255文字以内で入力してください。")
    private String title;

    @Size(max = 65535, message = "シーン本文は65535文字以内で入力してください。")
    private String content;

    @Size(max = 65535, message = "GM情報は65535文字以内で入力してください。")
    private String gmInfo;

    private MultipartFile imageFile;

    private String existingImagePath;
}
