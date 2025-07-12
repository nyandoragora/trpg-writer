package com.example.trpg_writer.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "scene_infos")
@IdClass(SceneInfoId.class)
@Data
public class SceneInfo {
    @Id
    @ManyToOne
    @JoinColumn(name = "scene_id")
    private Scene scene;

    @Id
    @ManyToOne
    @JoinColumn(name = "info_id")
    private Info info;

    @Column(name = "condition")
    private String condition;
}
