package com.example.trpg_writer.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NpcPartId implements Serializable {
    private Integer npc;  // 参照するエンティティのフィールド名と一致させる
    private Integer part; // 参照するエンティティのフィールド名と一致させる
}
