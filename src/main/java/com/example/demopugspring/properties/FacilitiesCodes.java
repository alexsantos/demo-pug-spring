package com.example.demopugspring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/facilities.properties", encoding = "utf-8")
public class FacilitiesCodes implements CodesInterface {
	private final static String SUFFIX = "facility";

	@Autowired
	private Environment env;

	@Override
	public String getDecodeCode(String encodedCode) {
		return env.getProperty(SUFFIX + "." + encodedCode);
	}
}
