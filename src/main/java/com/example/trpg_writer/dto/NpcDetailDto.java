package com.example.trpg_writer.dto;

import java.util.List;
import lombok.Data;

@Data
public class NpcDetailDto {
    private Integer id;
    private String name;
    private String description;
    private Integer level;
    private String intelligence;
    private String perception;
    private String position;
    private String language;
    private String popularity;
    private String weakness;
    private Integer preemptive;
    private String movement;
    private Integer lifeResist;
    private Integer mindResist;
    private Integer impurity;
    private String habitat;

    private List<PartDto> parts;
    private List<SkillDto> skills;
    private List<BootyDto> bootys;

    @Data
    public static class PartDto {
        private String name;
        private Integer hit;
        private String damage;
        private Integer evasion;
        private Integer defense;
        private Integer hitPoint;
        private Integer magicPoint;
    }

    @Data
    public static class SkillDto {
        private String name;
        private String content;
    }

    @Data
    public static class BootyDto {
        private String diceNum;
        private String content;
    }
}
