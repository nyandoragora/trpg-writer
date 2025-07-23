package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.SceneInfo;
import com.example.trpg_writer.entity.SceneInfoId;
import com.example.trpg_writer.form.InfoForm;
import com.example.trpg_writer.repository.InfoRepository;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.repository.SceneRepository;
import com.example.trpg_writer.repository.SceneInfoRepository;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class InfoService {

    private final InfoRepository infoRepository;
    private final ScenarioRepository scenarioRepository;
    private final SceneRepository sceneRepository;
    private final SceneInfoRepository sceneInfoRepository;

    public Optional<Info> findById(Integer id) {
        return infoRepository.findById(id);
    }

    public List<Info> findByScenarioId(Integer scenarioId) {
        return infoRepository.findByScenarioId(scenarioId);
    }

    @Transactional
    public Info create(InfoForm infoForm) {
        Info info = new Info();
        Scenario scenario = scenarioRepository.findById(infoForm.getScenarioId())
            .orElseThrow(() -> new IllegalArgumentException("Scenario not found"));
        Scene scene = sceneRepository.findById(infoForm.getSceneId())
            .orElseThrow(() -> new IllegalArgumentException("Scene not found"));

        info.setScenario(scenario);

        // Sanitize name and content
        Safelist safelist = Safelist.none(); // For plain text
        info.setName(Jsoup.clean(infoForm.getName() != null ? infoForm.getName() : "", safelist));
        info.setContent(Jsoup.clean(infoForm.getContent() != null ? infoForm.getContent() : "", safelist));

        Info savedInfo = infoRepository.save(info);

        // Create SceneInfo entry to link Info to Scene
        SceneInfo sceneInfo = new SceneInfo();
        SceneInfoId sceneInfoId = new SceneInfoId();
        sceneInfoId.setScene(scene.getId());
        sceneInfoId.setInfo(savedInfo.getId());
        sceneInfo.setScene(scene);
        sceneInfo.setInfo(savedInfo);
        sceneInfo.setDisplayCondition(""); // Set a default empty string for displayCondition
        sceneInfoRepository.save(sceneInfo);

        return savedInfo;
    }

    @Transactional
    public Info update(Info info, InfoForm infoForm) {
        // Sanitize name and content
        Safelist safelist = Safelist.none(); // For plain text
        info.setName(Jsoup.clean(infoForm.getName() != null ? infoForm.getName() : "", safelist));
        info.setContent(Jsoup.clean(infoForm.getContent() != null ? infoForm.getContent() : "", safelist));

        Info updatedInfo = infoRepository.save(info);

        // Update SceneInfo entry if necessary (e.g., if sceneId changed, though not expected here)
        // For now, assuming sceneId doesn't change for an existing info
        return updatedInfo;
    }

    @Transactional
    public void delete(Integer id) {
        // Delete associated SceneInfo entries first
        sceneInfoRepository.deleteByInfoId(id);
        infoRepository.deleteById(id);
    }
}