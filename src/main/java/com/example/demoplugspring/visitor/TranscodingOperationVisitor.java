package com.example.demoplugspring.visitor;

import org.springframework.stereotype.Service;

import com.example.demopugspring.properties.CodesInterface;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Primitive;

@Service
public class TranscodingOperationVisitor extends MapperVisitor {
	
	CodesInterface codeInterface;
	
	public TranscodingOperationVisitor(String path, String value, CodesInterface codeInterface) throws HL7Exception {
		super(path, value);
		this.codeInterface = codeInterface;
	}
	
	
	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
	
		type.setValue(decodeFieldsCodes(type.getValue(), codeInterface));
		return false;
	}
	
	private String decodeFieldsCodes(String valueToDecode, CodesInterface codeInterface) throws HL7Exception {
		
		return codeInterface.getDecodeCode(valueToDecode);
	}

}
