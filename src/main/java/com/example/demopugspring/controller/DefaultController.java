package com.example.demopugspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        model.addAttribute("title", "Titulo");
        logger.info("Index...");
        return "index";
    }

}
