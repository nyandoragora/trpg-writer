package com.example.trpg_writer.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "npcs")
@Data
public class Npc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "level")
    private Integer level;

    @Column(name = "intelligence")
    private Integer intelligence;

    @Column(name = "perception")
    private Integer perception;

    @Column(name = "position")
    private String position;

    @Column(name = "language")
    private String language;

    @Column(name = "popularity")
    private Integer popularity;

    @Column(name = "weakness")
    private String weakness;

    @Column(name = "preemptive")
    private String preemptive;

    @Column(name = "movement")
    private String movement;

    @Column(name = "life_resist")
    private Integer lifeResist;

    @Column(name = "mind_resist")
    private Integer mindResist;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}
