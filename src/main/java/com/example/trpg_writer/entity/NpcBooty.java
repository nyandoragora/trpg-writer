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
@Table(name = "npc_bootys")
@IdClass(NpcBootyId.class)
@Data
@ToString(exclude = {"npc", "booty"})
public class NpcBooty {
    @Id
    @ManyToOne
    @JoinColumn(name = "npc_id")
    @JsonIgnore
    private Npc npc;

    @Id
    @ManyToOne
    @JoinColumn(name = "booty_id")
    private Booty booty;
}