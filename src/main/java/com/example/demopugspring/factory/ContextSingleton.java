package com.example.demopugspring.factory;

import ca.uhn.hl7v2.DefaultHapiContext;
import ca.uhn.hl7v2.HapiContext;

public class ContextSingleton {

	private static HapiContext singleton;

	private ContextSingleton() {
	}

	public static HapiContext getInstance() {
		if (singleton == null) {
			singleton = new DefaultHapiContext();
		}
		return singleton;
	}
}
