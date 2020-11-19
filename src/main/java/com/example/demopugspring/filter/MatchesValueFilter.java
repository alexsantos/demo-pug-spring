package com.example.demopugspring.filter;

import ca.uhn.hl7v2.model.Primitive;

public class MatchesValueFilter implements Filter {

	String valueToCheck;

	public MatchesValueFilter(String valueToCheck) {
		this.valueToCheck = valueToCheck;
	}
	@Override
	public boolean doFilter(Primitive primitive) {
		boolean primitiveHasCondition = false;
		if (primitive.getValue() == null && valueToCheck.isBlank()) {
			primitiveHasCondition = true;
		} else {
			primitiveHasCondition = primitive.getValue().matches(valueToCheck);
		}
		return primitiveHasCondition;
	}

}
