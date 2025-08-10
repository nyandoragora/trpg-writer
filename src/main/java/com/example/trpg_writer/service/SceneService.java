package com.example.trpg_writer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.repository.SceneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneRepository sceneRepository;
    private final ScenarioRepository scenarioRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/scenes/";

    @Transactional
    public Scene create(SceneForm sceneForm, Integer scenarioId) {
        Scenario scenario = scenarioRepository.findById(scenarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Scenario not found"));
        Scene scene = new Scene();
        scene.setTitle(sceneForm.getTitle());
        
        Safelist safelist = Safelist.basicWithImages()
                                     .addTags("div", "table", "thead", "tbody", "tr", "th", "td")
                                     .addAttributes("div", "class");
        String safeContent = Jsoup.clean(sceneForm.getContent() != null ? sceneForm.getContent() : "", safelist);
        scene.setContent(safeContent);

        String safeGmInfo = Jsoup.clean(sceneForm.getGmInfo() != null ? sceneForm.getGmInfo() : "", safelist);
        scene.setGmInfo(safeGmInfo);
        
        scene.setScenario(scenario);
        return sceneRepository.save(scene);
    }

        @Transactional
    public void update(SceneForm sceneForm, Integer sceneId, Integer scenarioId) {
        Scene scene = sceneRepository.findById(sceneId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        
        // Make sure the scene belongs to the correct scenario
        if (!scene.getScenario().getId().equals(scenarioId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Scene does not belong to the specified scenario");
        }

        scene.setTitle(sceneForm.getTitle());
        scene.setContent(sceneForm.getContent());
        scene.setGmInfo(sceneForm.getGmInfo());
        
        sceneRepository.save(scene);
    }

    @Transactional
    public void saveImage(Integer sceneId, MultipartFile imageFile) {
        try {
            Scene scene = sceneRepository.findById(sceneId)
                .orElseThrow(() -> new IllegalArgumentException("Scene not found with ID: " + sceneId));

            if (!imageFile.isEmpty()) {
                Path uploadPath = Paths.get(UPLOAD_DIR);
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = imageFile.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
                Path filePath = uploadPath.resolve(uniqueFilename);

                Files.copy(imageFile.getInputStream(), filePath);

                scene.setImagePath("/images/scenes/" + uniqueFilename);
                sceneRepository.save(scene);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public Page<Scene> findByScenarioOrderByCreatedAtAsc(Scenario scenario , Pageable pageable) {
        return sceneRepository.findByScenarioOrderByCreatedAtAsc(scenario , pageable);
    }

    public List<Scene> findByScenarioOrderByCreatedAtAsc(Scenario scenario) {
        return sceneRepository.findByScenarioOrderByCreatedAtAsc(scenario);
    }

    public List<Scene> findByScenario(Scenario scenario) {
        return sceneRepository.findByScenario(scenario);
    }

    public Optional<Scene> findById(Integer id) {
        return sceneRepository.findById(id);
    }
}