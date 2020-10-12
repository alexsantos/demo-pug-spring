package com.example.demopugspring.controller;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class ApplicationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ApplicationService applicationService;

    @GetMapping(value = "/applications")
    public String getApps(Model model) {
        List<Application> applications = applicationService.findAll();
        model.addAttribute("applications", applications);
        return "application-list";
    }

    @GetMapping(value = "/applications/{id}")
    public String getApp(Model model, @PathVariable(name = "id") Long id) {
        Application application = applicationService.findById(id);
        model.addAttribute("application", application);
        return "application";
    }

    @GetMapping(value = {"/applications/add"})
    public String showAddApplication(Model model) {
        Application application = new Application();
        model.addAttribute("add", true);
        model.addAttribute("application", application);
        return "application-edit";
    }

    @PostMapping(value = "/applications/add")
    public String addApplication(Model model,
                          @ModelAttribute("application") Application application) {
        try {
            logger.info(application.toString());
            Application newApp = applicationService.save(application);
            return "redirect:/applications/" + String.valueOf(newApp.getId());
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "application-edit";
        }
    }
}
