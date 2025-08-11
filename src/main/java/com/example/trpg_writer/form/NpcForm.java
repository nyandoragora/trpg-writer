package com.example.trpg_writer.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List; // Listをインポート

@Data
public class NpcForm {
    private Integer id; // 編集時に使用

    @NotNull(message = "シナリオIDは必須です。")
    private Integer scenarioId;

    private Integer sceneId; // どのシーンに戻るかを保持するために追加

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

    // 説明
    private String description;

    // 攻撃方法、特殊能力、戦利品は、IDのリストで保持するように変更
    private List<Integer> partIds = new ArrayList<>(); // 攻撃方法・部位のIDリスト
    private List<Integer> skillIds = new ArrayList<>(); // 特殊能力のIDリスト
    private List<Integer> bootyIds = new ArrayList<>(); // 戦利品のIDリスト
}
