package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEditForm {
    
    @NotBlank(message = "名前を入力してください。")
    private String name;

    private String introduction;

}
