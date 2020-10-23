package com.example.demopugspring.controller;

import ca.uhn.hl7v2.HL7Exception;
import com.example.demopugspring.engine.MapperEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DefaultController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MapperEngine mapperEngine;

    @GetMapping(value = {"/", "/index"})
    public String index(Model model) {
        logger.info("Index...");
        model.addAttribute("source", null);
        return "index";
    }

    @PostMapping(value = {"/", "/index"})
    public String invoke(Model model,
                         @RequestParam("source") String source) throws HL7Exception {
        logger.info("invoke...");
        model.addAttribute("source", source);
        String response;
        try {
            response = mapperEngine.invoke(source);
        } catch (HL7Exception e) {
            e.printStackTrace();
            response = "ERROR";
        }
        model.addAttribute("response", response);
        return "index";
    }

}
