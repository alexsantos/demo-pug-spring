package com.example.demopugspring.service;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.repository.ApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ApplicationService {

    @Autowired
    private ApplicationRepository applicationRepository;

    public List<Application> findAll() {
        List<Application> applications = new ArrayList<>();
        applicationRepository.findAll().forEach(applications::add);
        return applications;
    }
    public Application findById(Long id) {
        return applicationRepository.findById(id).orElseThrow();
    }
    public Application save(Application application) {
        return applicationRepository.save(application);
    }

    public Application findByCode(String code) {
        return applicationRepository.findByCode(code);
    }
}
