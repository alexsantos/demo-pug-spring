package com.example.demopugspring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class Codes {

	@Autowired
	private Environment env;
	
	public String getDecodeCode(String encodedCode) {
		return env.getProperty(getSuffix() + "." + encodedCode);
	}

	protected abstract String getSuffix();
}
