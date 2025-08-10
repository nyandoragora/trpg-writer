package com.example.trpg_writer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "scene_infos")
@IdClass(SceneInfoId.class)
@Data
@ToString(exclude = {"scene", "info"})
public class SceneInfo {
    @Id
    @ManyToOne
    @JoinColumn(name = "scene_id")
    @JsonIgnore
    private Scene scene;

    @Id
    @ManyToOne
    @JoinColumn(name = "info_id")
    private Info info;

    @Column(name = "display_condition")
    private String displayCondition;
}