package com.example.trpg_writer.service;

import com.example.trpg_writer.entity.Faq;
import com.example.trpg_writer.repository.FaqRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public final class FaqService {

    private final FaqRepository faqRepository;

    public List<Faq> findAll() {
        return faqRepository.findAll();
    }
}
