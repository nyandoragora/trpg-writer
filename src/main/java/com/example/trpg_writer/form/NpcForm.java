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

    private Integer level;

    private String intelligence;

    private String perception;

    private String position;

    private Integer impurity;

    private String language;

    private String habitat;

    private String popularity;



    private String weakness;

    private Integer preemptive;

    private String movement;

    private Integer lifeResist;

    private Integer mindResist;

    // 説明
    private String description;

    // 攻撃方法、特殊能力、戦利品は、IDのリストで保持するように変更
    private List<Integer> partIds = new ArrayList<>(); // 攻撃方法・部位のIDリスト
    private List<Integer> skillIds = new ArrayList<>(); // 特殊能力のIDリスト
    private List<Integer> bootyIds = new ArrayList<>(); // 戦利品のIDリスト
}
