package com.example.demopugspring.properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/facilities.properties", encoding = "utf-8")
public class FacilitiesCodes extends Codes {
	private final static String SUFFIX = "facility";

	public static String getSuffix() {
		return SUFFIX;
	}
}
