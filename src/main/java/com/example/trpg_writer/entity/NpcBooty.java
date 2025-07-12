package com.example.trpg_writer.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "npc_bootys")
@IdClass(NpcBootyId.class)
@Data
public class NpcBooty {
    @Id
    @ManyToOne
    @JoinColumn(name = "npc_id")
    private Npc npc;

    @Id
    @ManyToOne
    @JoinColumn(name = "booty_id")
    private Booty booty;
}
