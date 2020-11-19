package com.example.demopugspring.visitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Primitive;

public class ReplaceVisitor extends MapperVisitor {

	private String regex;
	private String valueToReplace;

	public ReplaceVisitor(String path, String valueToReplace, String regex) throws HL7Exception {
		super(path, valueToReplace);
		this.regex = regex;
		this.valueToReplace = valueToReplace;

	}

	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		type.setValue(type.getValue().replaceAll(regex, valueToReplace));
		return false;
	}

}
