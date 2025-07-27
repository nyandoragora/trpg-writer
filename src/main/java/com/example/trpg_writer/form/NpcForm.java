package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class NpcForm {
    private Integer id; 

    @NotNull(message = "シナリオIDは必須です。") 
    private Integer scenarioId;

    @NotBlank(message = "名前は必須です。")
    private String name;

    @NotNull(message = "レベルは必須です。")
    private Integer level;

    @NotBlank(message = "知能は必須です。")
    private String intelligence;

    @NotBlank(message = "知覚は必須です。")
    private String perception;

    @NotBlank(message = "反応は必須です。") 
    private String position; 

    @NotNull(message = "穢れは必須です。")
    private Integer impurity;

    @NotBlank(message = "言語は必須です。")
    private String language;

    @NotBlank(message = "生息地は必須です。")
    private String habitat;

    @NotBlank(message = "知名度は必須です。") 
    private String popularity; 

    private String weakness; 

    @NotNull(message = "先制値は必須です。")
    private Integer preemptive;

    @NotBlank(message = "移動速度は必須です。")
    private String movement; 

    @NotNull(message = "生命抵抗力は必須です。")
    private Integer lifeResist; 

    @NotNull(message = "精神抵抗力は必須です。")
    private Integer mindResist; 

    
    private String description;

    
    private String attackMethod;
    private String specialAbilities;
    private String booty;
}
