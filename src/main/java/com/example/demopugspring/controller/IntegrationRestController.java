package com.example.demopugspring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.Response;

@RestController
public class IntegrationRestController {
	private static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);
	@Autowired
	MapperEngine mapperEngine;

	@PostMapping(value = "/mapper", consumes = "text/plain", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> map(@RequestBody String msg) {
		Response response = mapperEngine.run(msg);
		return response.getErrorList().isEmpty() ? new ResponseEntity<>(response, HttpStatus.OK)
				: new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
