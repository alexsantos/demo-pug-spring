package com.example.demopugspring.controller;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.HapiContext;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;
import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.factory.ContextSingleton;
import com.example.demopugspring.model.Integration;
import com.example.demopugspring.model.Mapper;
import com.example.demopugspring.service.ApplicationService;
import com.example.demopugspring.service.IntegrationService;
import com.example.demopugspring.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class IntegrationRestController {
    private static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);
    @Autowired
    MapperEngine mapperEngine;
    @PostMapping(value = "/mapper", consumes = "application/hl7-v2", produces = MediaType.TEXT_PLAIN_VALUE)
    public String map(@RequestBody String msg) {
        String result = "";
        try {
            result = mapperEngine.invoke(msg);
        } catch (HL7Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
