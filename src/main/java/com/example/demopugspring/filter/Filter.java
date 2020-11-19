package com.example.demopugspring.filter;

import ca.uhn.hl7v2.model.Primitive;

public interface Filter {
	public boolean doFilter(Primitive primitive);
}
