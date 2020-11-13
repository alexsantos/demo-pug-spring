package com.example.demopugspring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/Country_GH_ehCOS.properties", encoding = "utf-8")
public class CountryCodes implements CodesInterface {

	@Autowired
	private Environment env;

	@Override
	public String getDecodeCode(String encodedCode) {
		return env.getProperty(encodedCode);
	}
}
