package com.example.demopugspring.controller;

import com.example.demopugspring.helper.PugHelper;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MapperService;
import com.example.demopugspring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class IntegrationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    IntegrationService integrationService;
    @Autowired
    MapperService mapperService;
    @Autowired
    MessageService messageService;
    @Autowired
    ApplicationService applicationService;

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
        model.addAttribute("PugHelper", new PugHelper());
        return "integrations/details";
    }

    @GetMapping(value = {"/integrations/create"})
    public String showAddIntegration(Model model) {
        model.addAttribute("messages", messageService.findAll());
        model.addAttribute("applications", applicationService.findAll());
        return "/integrations/create";
    }

    @PostMapping(value = "/integrations/create")
    public String addIntegration(Model model,
                                 @ModelAttribute("integration") Integration integration) {
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

    @PostMapping(value = "/integrations/update")
    public String updateIntegration(Model model,
                                    @ModelAttribute("id") Long id,
                                    @RequestParam("mappers") List<Long> mappers) {
        try {
            logger.info("Integration ID:" + id);
            logger.info(mappers.toString());
            integrationService.updateMappers(id, mappers);
            model.addAttribute("success", "1");
            return "redirect:/integrations/".concat(String.valueOf(id));
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            return "/integrations/".concat(String.valueOf(id));
        }
    }
}
