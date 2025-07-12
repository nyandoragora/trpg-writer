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
@Table(name = "scene_npcs")
@IdClass(SceneNpcId.class)
@Data
public class SceneNpc {
    @Id
    @ManyToOne
    @JoinColumn(name = "scene_id")
    private Scene scene;

    @Id
    @ManyToOne
    @JoinColumn(name = "npc_id")
    private Npc npc;

    @Column(name = "appearance_notes")
    private String appearanceNotes;
}
