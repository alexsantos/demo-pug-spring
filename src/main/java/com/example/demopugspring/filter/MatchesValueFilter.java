package com.example.demopugspring.filter;

import java.util.StringTokenizer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.util.Terser;

public class MatchesValueFilter implements Filter {

    private static final String PARSE_TOKEN = "=";
    private static final String NOT_OPERATOR = "!";
    private String valueToCheck;
    private boolean negativize;

    public MatchesValueFilter(String value, Terser tmp) throws HL7Exception {

        this.valueToCheck = parseValue(value, tmp);
        negativize = value.contains(NOT_OPERATOR);
        this.valueToCheck = valueToCheck.replace(NOT_OPERATOR, "");
	}

    private String parseValue(String value, Terser tmp) throws HL7Exception {
		String token;
		String fieldValue;

		StringTokenizer tok = new StringTokenizer(value, PARSE_TOKEN + PARSE_TOKEN, false);
		while (tok.hasMoreTokens()) {
			token = tok.nextToken();

			if (token.length() <= 1) {
				continue;
			}

			try {
				fieldValue = tmp.get(token);
				if (fieldValue == null) {
					fieldValue = "";
				}
				value = value.replaceAll(PARSE_TOKEN + token + PARSE_TOKEN, fieldValue);
			} catch (HL7Exception e) {
				// DO NOTHING TOKEN IT'S NOT A HL/ PATH
			}
		}
        return value;
    }

	@Override
	public boolean doFilter(Primitive primitive) {
		boolean primitiveHasCondition = false;
		if (primitive.getValue() == null) {
            primitiveHasCondition = valueToCheck.isBlank();
		} else {
			primitiveHasCondition = primitive.getValue().matches(valueToCheck);
		}
        return primitiveHasCondition ^ negativize;
	}

}
