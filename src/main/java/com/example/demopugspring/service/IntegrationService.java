package com.example.demopugspring.service;

import com.example.demopugspring.model.Integration;
import com.example.demopugspring.repository.IntegrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class IntegrationService {

    @Autowired
    private IntegrationRepository integrationRepository;

    public List<Integration> findAll() {
        List<Integration> integrations = new ArrayList<>();
        integrationRepository.findAll().forEach(integrations::add);
        return integrations;
    }
    public Integration findById(Long id) {
        return integrationRepository.findById(id).orElseThrow();
    }
    public Integration save(Integration integration) {
        return integrationRepository.save(integration);
    }
}
