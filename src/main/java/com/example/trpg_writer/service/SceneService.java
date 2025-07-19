package com.example.trpg_writer.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.example.trpg_writer.entity.Scene;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.form.SceneForm;
import com.example.trpg_writer.repository.SceneRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SceneService {

    private final SceneRepository sceneRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/scenes/";

    // Method to save a Scene entity
    @Transactional
    public Scene save(Scene scene) {
        return sceneRepository.save(scene);
    }

    // Method to create a new Scene
    @Transactional
    public Scene create(SceneForm sceneForm, Scenario scenario) {
        Scene scene = new Scene();
        scene.setTitle(sceneForm.getTitle());
        scene.setContent(""); // Initialize content with an empty string
        scene.setScenario(scenario);
        return sceneRepository.save(scene);
    }

    // Method to save an image for a scene
    @Transactional
    public void saveImage(Integer sceneId, MultipartFile imageFile) throws IOException {
        Scene scene = sceneRepository.findById(sceneId)
            .orElseThrow(() -> new IllegalArgumentException("Scene not found with ID: " + sceneId));

        if (!imageFile.isEmpty()) {
            // Create the upload directory if it doesn't exist
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Generate a unique filename
            String originalFilename = imageFile.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String uniqueFilename = UUID.randomUUID().toString() + fileExtension;
            Path filePath = uploadPath.resolve(uniqueFilename);

            // Save the file to the file system
            Files.copy(imageFile.getInputStream(), filePath);

            // Update the scene's image path
            scene.setImagePath("/images/scenes/" + uniqueFilename); // Store relative path for web access
            sceneRepository.save(scene);
        }
    }

    public Page<Scene> findByScenarioOrderByCreatedAtAsc(Scenario scenario , Pageable pageable) {
        return sceneRepository.findByScenarioOrderByCreatedAtAsc(scenario , pageable);
    }

    public List<Scene> findByScenario(Scenario scenario) {
        return sceneRepository.findByScenario(scenario);
    }

    public Optional<Scene> findById(Integer id) {
        return sceneRepository.findById(id);
    }
}
