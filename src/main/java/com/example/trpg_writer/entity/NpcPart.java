package com.example.trpg_writer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "npc_parts")
@IdClass(NpcPartId.class)
@Data
public class NpcPart {
    @Id
    @ManyToOne
    @JoinColumn(name = "npc_id")
    @JsonIgnore
    private Npc npc;

    @Id
    @ManyToOne
    @JoinColumn(name = "part_id")
    private Part part;
}
