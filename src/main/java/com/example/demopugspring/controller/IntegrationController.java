package com.example.demopugspring.controller;

import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class IntegrationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IntegrationService integrationService;
    @Autowired
    MapperService mapperService;

    @GetMapping(value = "/integrations")
    public String getIntegrations(Model model) {
        List<Integration> integrations = integrationService.findAll();
        model.addAttribute("integrations", integrations);
        logger.info(integrations.toString());
        return "integrations/index";
    }

    @GetMapping(value = "/integrations/{id}")
    public String getIntegration(Model model, @PathVariable(name = "id") Long id) {
        Integration integration = integrationService.findById(id);
        List<Mapper> mappers = mapperService.findAll();
        model.addAttribute("integration", integration);
        model.addAttribute("mappers", mappers);
        return "integrations/details";
    }

    @GetMapping(value = {"/integrations/create"})
    public String showAddIntegration(Model model) {
        Integration integration = new Integration();
        model.addAttribute("integration", integration);
        return "/integrations/create";
    }

    @PostMapping(value = "/integrations/create")
    public String addIntegration(Model model,
                                 @ModelAttribute("message") Integration integration) {
        try {
            logger.info(integration.toString());
            integrationService.save(integration);
            return "redirect:/integrations";
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "/integrations";
        }
    }
}
