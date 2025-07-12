package com.example.trpg_writer.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SceneNpcId implements Serializable {
    private Integer scene; // 参照するエンティティのフィールド名と一致させる
    private Integer npc;   // 参照するエンティティのフィールド名と一致させる
}
