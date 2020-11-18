package com.example.demopugspring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/IdentificationCodes.properties", encoding = "utf-8")
public class IdentificationCodes implements CodesInterface {
	private final static String SUFFIX = "identification";

	@Autowired
	private Environment env;
	
	@Override
	public String getDecodeCode(String encodedCode) {
		return env.getProperty(SUFFIX + "." + encodedCode);
	}

}
