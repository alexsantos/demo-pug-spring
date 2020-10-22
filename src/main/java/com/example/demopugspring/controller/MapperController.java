package com.example.demopugspring.controller;

import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.model.Message;
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
public class MapperController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    MapperService mapperService;

    @GetMapping(value = "/mappers")
    public String getMaps(Model model) {
        List<Mapper> mappers = mapperService.findAll();
        model.addAttribute("mappers", mappers);
        logger.info(mappers.toString());
        return "mappers/index";
    }

    @GetMapping(value = {"/mappers/create"})
    public String showAddMapper(Model model) {
        Mapper mapper = new Mapper();
        model.addAttribute("mapper", mapper);
        return "/mappers/create";
    }

    @PostMapping(value = {"/mappers/create", "/mappers/update"})
    public String addMapper(Model model,
                                 @ModelAttribute("mapper") Mapper mapper) {
        try {
            logger.info(mapper.toString());
            mapperService.save(mapper);
            return "redirect:/mappers";
        } catch (Exception ex) {
            // log exception first,
            // then show error
            String errorMessage = ex.getMessage();
            logger.error(errorMessage);
            model.addAttribute("errorMessage", errorMessage);
            model.addAttribute("add", true);
            return "/mappers";
        }
    }

    @GetMapping(value = {"/mappers/edit"})
    public String showEditMapper(Model model,
                                    @RequestParam("id") Long id) {
        Mapper mapper = mapperService.findById(id);
        model.addAttribute("mapper", mapper);
        return "/mappers/edit";
    }

}
