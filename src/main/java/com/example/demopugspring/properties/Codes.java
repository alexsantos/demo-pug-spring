package com.example.demopugspring.properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

public abstract class Codes {

	@Autowired
	private Environment env;

	public String getDecodeCode(String encodedCode) {
		String result;
		if (encodedCode == null) {
			encodedCode = "";
		}

		if (getSuffix().isBlank()) {
			result = env.getProperty(encodedCode);
		} else {
			result = env.getProperty(getSuffix() + "." + encodedCode);
		}
		return result;
	}


	protected abstract String getSuffix();
}
