package com.example.demopugspring.properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/MarriageStatus.properties", encoding = "utf-8")
public class MarriageStatusCodes extends Codes {
	private final static String SUFFIX = "marriage.status";

	public String getSuffix() {
		return SUFFIX;
	}
}
