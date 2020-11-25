package com.example.demopugspring.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demopugspring.model.IntegrationMapper;
import com.example.demopugspring.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demopugspring.helper.PugHelper;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;

@Controller
public class IntegrationController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	IntegrationMapperService integrationMapperService;
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
		List<IntegrationMapper> integrationMappers = integrationMapperService.retrieveIntegrationMappersFromIntegration(integration);
		ArrayList<Long> mappersIndex = new ArrayList<>();
		List<Mapper> mappers = new ArrayList<>();
		Mapper mapper;
		for(IntegrationMapper intMapper : integrationMappers){
			mappersIndex.add(intMapper.getMapper().getId());
			mapper = intMapper.getMapper();
			mapper.setActive(intMapper.isActive());
			mappers.add(mapper);
		}
		List<Mapper> mappersNotIncluded = mapperService.findALlByIdNotIn(mappersIndex);
		model.addAttribute("integration", integration);
		model.addAttribute("mappersIncluded", mappers);
		model.addAttribute("mappersNotIncluded", mappersNotIncluded);
		model.addAttribute("PugHelper", new PugHelper());
		return "integrations/details";
	}

	@GetMapping(value = {"/integrations/create"})
	public String showAddIntegration(Model model) {
		model.addAttribute("messages", messageService.findAll());
		model.addAttribute("applications", applicationService.findAll());
		return "integrations/create";
	}

	@GetMapping(value = {"/integrations/edit"})
	public String showEditIntegration(Model model, @RequestParam("id") Long id) {
		Mapper mapper;
		Integration integration = integrationService.findById(id);
		List<IntegrationMapper> integrationMappers = integrationMapperService.retrieveIntegrationMappersFromIntegration(integration);
		List<Mapper> mappers = new ArrayList<>();

		for (IntegrationMapper integrationMapper : integrationMappers) {
			mapper = integrationMapper.getMapper();
			mapper.setActive(integrationMapper.isActive());
			mappers.add(mapper);
		}
		model.addAttribute("messages", messageService.findAll());
		model.addAttribute("applications", applicationService.findAll());
		model.addAttribute("integration", integration);
		model.addAttribute("mappers", mappers);
		return "integrations/edit";
	}

	@PostMapping(value = {"/integrations/create", "/integrations/edit"})
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
			return "integrations/index";
		}
	}

	@PostMapping(value = "/integrations/update")
	public String updateIntegration(Model model,
									@ModelAttribute("id") Long id,
	        @RequestParam(value = "mappers", required = false, defaultValue = "") List<Long> mappers,
	        @RequestParam(value = "activeMappers", required = false, defaultValue = "") List<Long> activeMappers) {
		try {
			logger.info("Integration ID:" + id);
			logger.info(mappers.toString());
			integrationService.updateMappers(id, mappers, activeMappers);
			model.addAttribute("success", "1");
			return "redirect:/integrations/".concat(String.valueOf(id));
		} catch (Exception ex) {
			// log exception first,
			// then show error
			String errorMessage = ex.getMessage();
			logger.error(errorMessage);
			model.addAttribute("errorMessage", errorMessage);
			return "integrations/".concat(String.valueOf(id));
		}
	}
}
