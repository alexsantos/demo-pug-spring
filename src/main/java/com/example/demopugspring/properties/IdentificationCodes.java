package com.example.demopugspring.properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/IdentificationCodes.properties", encoding = "utf-8")
public class IdentificationCodes implements CodesInterface {
	private final static String SUFFIX = "identification";

	public static String getSuffix() {
		return SUFFIX;
	}
	

}
