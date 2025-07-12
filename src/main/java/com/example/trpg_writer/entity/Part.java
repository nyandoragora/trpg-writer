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
@Table(name = "parts")
@Data
public class Part {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "scenario_id")
    private Scenario scenario;

    @Column(name = "name")
    private String name;

    @Column(name = "hit")
    private Integer hit;

    @Column(name = "damage")
    private Integer damage;

    @Column(name = "evasion")
    private Integer evasion;

    @Column(name = "protection")
    private Integer protection;

    @Column(name = "life_point")
    private Integer lifePoint;

    @Column(name = "magic_point")
    private Integer magicPoint;

    @Column(name = "created_at", insertable = false, updatable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private Timestamp updatedAt;
}
