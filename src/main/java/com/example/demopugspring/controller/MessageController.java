package com.example.demopugspring.controller;

import com.example.demopugspring.model.Message;
import com.example.demopugspring.service.MessageService;
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
public class MessageController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MessageService messageService;

	@GetMapping(value = {"/messages", "/messages/index"})
	public String getMessages(Model model) {
		List<Message> messages = messageService.findAll();
		model.addAttribute("messages", messages);
		logger.info(messages.toString());
		return "messages/index";
	}

	@GetMapping(value = {"/messages/create"})
	public String showAddMessage(Model model) {
		Message message = new Message();
		model.addAttribute("message", message);
		model.addAttribute("versions", Message.Version.values());
		return "messages/create";
	}

	@PostMapping(value = {"/messages/create", "/messages/update"})
	public String addMessage(Model model,
							 @ModelAttribute("message") Message message) {
		try {
			logger.info(message.toString());
			messageService.save(message);
			return "redirect:/messages";
		} catch (Exception ex) {
			// log exception first,
			// then show error
			String errorMessage = ex.getMessage();
			logger.error(errorMessage);
			model.addAttribute("errorMessage", errorMessage);
			model.addAttribute("add", true);
			model.addAttribute("message", message);
			model.addAttribute("versions", Message.Version.values());
			return "messages/edit";
		}
	}

	@GetMapping(value = {"/messages/edit"})
	public String showEditMessage(Model model,
								  @RequestParam("id") Long id) {
		Message message = messageService.findById(id);
		model.addAttribute("message", message);
		model.addAttribute("versions", Message.Version.values());
		return "messages/edit";
	}

}
