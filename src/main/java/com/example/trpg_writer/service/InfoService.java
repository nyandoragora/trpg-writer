package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Info;
import com.example.trpg_writer.entity.Scenario;
import com.example.trpg_writer.entity.Role;
import com.example.trpg_writer.form.InfoForm;
import com.example.trpg_writer.repository.InfoRepository;
import com.example.trpg_writer.repository.ScenarioRepository;
import com.example.trpg_writer.repository.RoleRepository;

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

        info.setScenario(scenario);

        // Sanitize name and content
        Safelist safelist = Safelist.none(); // For plain text
        info.setName(Jsoup.clean(infoForm.getName() != null ? infoForm.getName() : "", safelist));
        info.setContent(Jsoup.clean(infoForm.getContent() != null ? infoForm.getContent() : "", safelist));

        return infoRepository.save(info);
    }

    @Transactional
    public Info update(Info info, InfoForm infoForm) {
        // Sanitize name and content
        Safelist safelist = Safelist.none(); // For plain text
        info.setName(Jsoup.clean(infoForm.getName() != null ? infoForm.getName() : "", safelist));
        info.setContent(Jsoup.clean(infoForm.getContent() != null ? infoForm.getContent() : "", safelist));

        return infoRepository.save(info);
    }

    @Transactional
    public void delete(Integer id) {
        infoRepository.deleteById(id);
    }
}
