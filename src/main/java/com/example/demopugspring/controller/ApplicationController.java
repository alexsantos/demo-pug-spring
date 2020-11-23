package com.example.demopugspring.controller;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.service.ApplicationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ApplicationController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ApplicationService applicationService;

	@GetMapping(value = {"/applications", "/applications/index"})
	public String getApps(Model model) {
		List<Application> applications = applicationService.findAll();
		model.addAttribute("applications", applications);
		return "applications/index";
	}

	@GetMapping(value = {"/applications/edit"})
	public String showEditApp(Model model, @RequestParam("id") Long id) {
		Application application = applicationService.findById(id);
		model.addAttribute("application", application);
		return "applications/edit";
	}

	@PostMapping(value = "/applications/edit")
	public String updateApp(Model model,
							@ModelAttribute("application") Application application) {
		try {
			logger.info("Updating application" + application.getName());
			applicationService.save(application);
			return "redirect:/applications";
		} catch (Exception ex) {
			// log exception first,
			// then show error
			String errorMessage = ex.getMessage();
			logger.error(errorMessage);
			model.addAttribute("errorMessage", errorMessage);
			return "applications/index";
		}
	}

	@GetMapping(value = {"/applications/create"})
	public String showAddApplication(Model model) {
		Application application = new Application();
		model.addAttribute("add", true);
		model.addAttribute("application", application);
		return "applications/create";
	}

	@PostMapping(value = "/applications/create")
	public String addApplication(Model model,
								 @ModelAttribute("application") Application application) {
		try {
			logger.info(application.toString());
			applicationService.save(application);
			return "redirect:/applications";
		} catch (Exception ex) {
			// log exception first,
			// then show error
			String errorMessage = ex.getMessage();
			logger.error(errorMessage);
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("add", true);
			return "applications/index";
		}
	}
}
