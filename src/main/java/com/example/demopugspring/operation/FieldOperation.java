package com.example.demopugspring.operation;

import java.util.ArrayList;
import java.util.List;

import com.example.demopugspring.visitor.RetrievePrimitivesVisitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;

public class FieldOperation implements Operation {
	String sourcePath;
	List<String> destinationsPath;
	ArrayList<Primitive> sourcePrimitives;
	
	
	public FieldOperation(String sourcePath, List<String> destinations){
		this.sourcePath = sourcePath;
		this.destinationsPath = destinations;
	}
	
	@Override
	public void doOperation(Message message) throws HL7Exception {
		RetrievePrimitivesVisitor source = new RetrievePrimitivesVisitor(sourcePath, null);
		
		source.start(message);
		sourcePrimitives = source.getPrimitives();
		
		for(String destPath : destinationsPath) {
			doOperation(message, destPath);
		}
	}
	
	@Override
	public void doOperation(Message message, String desPath) throws HL7Exception {
		Primitive sourcePrimitive;
		Primitive destPrimitive;
		ArrayList<Primitive> destPrimitives;
		RetrievePrimitivesVisitor destination;
		destination = new RetrievePrimitivesVisitor(desPath, null);
		destination.start(message);
		destPrimitives = destination.getPrimitives();
		
		for(int i = 0; i < sourcePrimitives.size(); i++){
			sourcePrimitive = sourcePrimitives.get(i);
			destPrimitive = destPrimitives.get(i);

			destPrimitive.setValue(sourcePrimitive.getValue());
		}
	}


}
