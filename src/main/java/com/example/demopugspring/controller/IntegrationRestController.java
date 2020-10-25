package com.example.demopugspring.controller;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IntegrationRestController {
    private static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);
    @Autowired
    MapperEngine mapperEngine;
    @PostMapping(value = "/mapper", consumes = "text/plain", produces = "application/json")
    public Response map(@RequestBody String msg) {
        Response response = new Response();
        response = mapperEngine.run(msg);
        return response;
    }

}
