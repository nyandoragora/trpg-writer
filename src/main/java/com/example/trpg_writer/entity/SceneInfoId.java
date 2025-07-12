package com.example.trpg_writer.entity;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SceneInfoId implements Serializable {
    private Integer scene; // 参照するエンティティのフィールド名と一致させる
    private Integer info;  // 参照するエンティティのフィールド名と一致させる
}
