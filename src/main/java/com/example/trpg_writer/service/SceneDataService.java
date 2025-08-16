package com.example.trpg_writer.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.dto.SceneEditPageData;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.form.NpcForm;
import com.example.trpg_writer.form.SceneForm;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public final class SceneDataService {

    private final ScenarioService scenarioService;
    private final SceneService sceneService;
    private final NpcService npcService;
    private final InfoService infoService;
    private final PartService partService;
    private final BootyService bootyService;
    private final SkillService skillService;
    private final SceneNpcService sceneNpcService;
    private final SceneInfoService sceneInfoService;
    private final NpcPartService npcPartService;
    private final NpcSkillService npcSkillService;
    private final NpcBootyService npcBootyService;
    private final RoleService roleService;

    @Value("${tinymce.api-key}")
    private String tinymceApiKey;

    public SceneEditPageData getSceneEditPageData(Integer scenarioId, Integer sceneId) {
        Scenario scenario = scenarioService.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Scene scene = sceneService.findById(sceneId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scene not found"));

        if (!scene.getScenario().getId().equals(scenario.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scene does not belong to the specified scenario");
        }

        SceneForm sceneForm = new SceneForm();
        sceneForm.setId(scene.getId());
        sceneForm.setTitle(scene.getTitle());
        sceneForm.setContent(scene.getContent());
        sceneForm.setGmInfo(scene.getGmInfo());
        sceneForm.setExistingImagePath(scene.getImagePath());

        List<String> infoNames = sceneInfoService.findBySceneId(sceneId).stream()
                .map(sceneInfo -> sceneInfo.getInfo().getName())
                .collect(Collectors.toList());

        NpcForm npcForm = new NpcForm();
        npcForm.setScenarioId(scenarioId);

        SceneEditPageData data = new SceneEditPageData();
        data.setSceneForm(sceneForm);
        data.setAllNpcs(npcService.findByScenarioId(scenarioId));
        data.setAllInfos(infoService.findByScenarioId(scenarioId));
        data.setInfoNames(infoNames);
        data.setAllParts(partService.findByScenarioId(scenarioId));
        data.setAllBootys(bootyService.findByScenarioId(scenarioId));
        data.setAllSkills(skillService.findByScenarioId(scenarioId));
        data.setSceneNpcs(sceneNpcService.findBySceneId(sceneId));
        data.setSceneInfos(sceneInfoService.findBySceneId(sceneId));
        data.setAllSceneInfos(sceneInfoService.findByScenarioId(scenarioId));
        data.setNpcParts(npcPartService.findByNpcScenarioId(scenarioId));
        data.setNpcSkills(npcSkillService.findByNpcScenarioId(scenarioId));
        data.setNpcBootys(npcBootyService.findByNpcScenarioId(scenarioId));
        data.setAllScenes(sceneService.findByScenario(scenario));
        data.setScenario(scenario);
        data.setScene(scene);
        data.setTinymceApiKey(tinymceApiKey);
        data.setNpcForm(npcForm);
        data.setGmRoleId(roleService.findByRole("ROLE_GM").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "GM Role not found")).getId());
        data.setPlRoleId(roleService.findByRole("ROLE_PL").orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "PL Role not found")).getId());

        return data;
    }
}
