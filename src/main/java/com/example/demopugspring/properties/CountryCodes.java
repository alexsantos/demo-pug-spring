package com.example.demopugspring.properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/Country_GH_ehCOS.properties", encoding = "utf-8")
public class CountryCodes extends Codes {
	private final static String SUFFIX = "country";

	public String getSuffix() {
		return SUFFIX;
	}

}
