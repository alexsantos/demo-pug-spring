package com.example.demopugspring.controller;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;
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

	@PostMapping(value = "/mapper", consumes = "text/plain", produces = "text/plain")
	public String map(@RequestBody String msg) throws Exception {
		Response response;
		response = mapperEngine.run(msg);
		if (response.getErrorList().size() > 0) {
			StringBuilder errorText = new StringBuilder("Found ");
			errorText.append(response.getErrorList().size()).append(" error(s).\n");
			for (MapperError error : response.getErrorList()) {
				errorText.append(error.getField())
						.append("::")
						.append(error.getError())
						.append("\n");
			}
			throw new Exception(errorText.toString());
		}
		return response.getMessage();
	}
}
