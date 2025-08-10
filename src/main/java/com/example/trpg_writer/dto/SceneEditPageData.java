package com.example.trpg_writer.dto;

import java.util.List;

import com.example.trpg_writer.entity.Booty;
import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.entity.Npc;
import com.example.trpg_writer.entity.NpcBooty;
import com.example.trpg_writer.entity.NpcPart;
import com.example.trpg_writer.entity.NpcSkill;
import com.example.trpg_writer.entity.Part;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneNpc;
import com.example.trpg_writer.entity.Skill;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.form.SceneForm;

import lombok.Data;

@Data
public class SceneEditPageData {
    private SceneForm sceneForm;
    private List<Npc> allNpcs;
    private List<Info> allInfos;
    private List<String> infoNames;
    private List<Part> allParts;
    private List<Booty> allBootys;
    private List<Skill> allSkills;
    private List<SceneNpc> sceneNpcs;
    private List<SceneInfo> sceneInfos;
    private List<SceneInfo> allSceneInfos;
    private List<NpcPart> npcParts;
    private List<NpcSkill> npcSkills;
    private List<NpcBooty> npcBootys;
    private List<Scene> allScenes;
    private Scenario scenario;
    private Scene scene;
    private String tinymceApiKey;
    private NpcForm npcForm;
    private Integer gmRoleId;
    private Integer plRoleId;
}
