package com.example.demopugspring.visitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Primitive;
import com.example.demopugspring.properties.Codes;


public class TranscodingVisitor extends MapperVisitor {

	Codes codeInterface;

	public TranscodingVisitor(String path, String value, Codes codeInterface) throws HL7Exception {
		super(path, value);
		this.codeInterface = codeInterface;
	}


	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		if (type != null) {
			type.setValue(decodeFieldsCodes(type.getValue(), codeInterface));
		}
		return false;
	}

	private String decodeFieldsCodes(String valueToDecode, Codes codeInterface) throws HL7Exception {

		return codeInterface.getDecodeCode(valueToDecode);
	}

}
