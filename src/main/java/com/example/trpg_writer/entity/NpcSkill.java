package com.example.trpg_writer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Entity
@Table(name = "npc_skills")
@IdClass(NpcSkillId.class)
@Data
@ToString(exclude = {"npc", "skill"})
public class NpcSkill {
    @Id
    @ManyToOne
    @JoinColumn(name = "npc_id")
    @JsonIgnore
    private Npc npc;

    @Id
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;
}