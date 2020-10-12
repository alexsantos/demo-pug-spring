package com.example.demopugspring.controller;

import com.example.demopugspring.model.Application;
import com.example.demopugspring.model.Message;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class MessageController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MessageService messageService;

    @GetMapping(value = "/messages")
    public String getMessages(Model model) {
        List<Message> messages = messageService.findAll();
        model.addAttribute("messages", messages);
        return "message-list";
    }

    @GetMapping(value = "/messages/{id}")
    public String getMessage(Model model, @PathVariable(name = "id") Long id) {
        Message message = messageService.findById(id);
        model.addAttribute("message", message);
        return "message";
    }
}
