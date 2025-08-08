package com.example.trpg_writer.entity;

import java.sql.Timestamp;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "parts")
@Data
@ToString(exclude = {"scenario", "npcs"})
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    @JsonIgnore
    private Scenario scenario;

    @Column(name = "name")
    private String name;

    @Column(name = "hit")
    private Integer hit;

    @Column(name = "damage")
    private String damage;

    @Column(name = "evasion")
    private Integer evasion;

    @Column(name = "protection")
    private Integer protection;

    @Column(name = "life_point")
    private Integer lifePoint;

    @Column(name = "magic_point")
    private Integer magicPoint;

    @OneToMany(mappedBy = "part", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<NpcPart> npcs;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}