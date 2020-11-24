package com.example.demopugspring.operation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Primitive;
import com.example.demopugspring.visitor.RetrievePrimitivesVisitor;

import java.util.ArrayList;
import java.util.List;

public class SwapOperation implements Operation {
	String sourcePath;
	List<String> destinationsPath;
	ArrayList<Primitive> sourcePrimitives;

	public SwapOperation(String sourcePath, List<String> destinations) {
		this.sourcePath = sourcePath;
		this.destinationsPath = destinations;
	}

	@Override
	public void doOperation(Message destMessage) throws HL7Exception {
		RetrievePrimitivesVisitor source = new RetrievePrimitivesVisitor(sourcePath, null);
		source.start(destMessage);
		sourcePrimitives = source.getPrimitives();

		for (String destPath : destinationsPath) {
			doOperation(destMessage, destPath);
		}

	}

	@Override
	public void doOperation(Message destMessage, String destPath) throws HL7Exception {
		String sourceValue;
		String destinationValue;
		Primitive sourcePrimitive;
		Primitive destPrimitive;
		ArrayList<Primitive> destPrimitives;
		RetrievePrimitivesVisitor destination;
		destination = new RetrievePrimitivesVisitor(destPath, null);
		destination.start(destMessage);
		destPrimitives = destination.getPrimitives();


		for (int i = 0; i < sourcePrimitives.size(); i++) {
			sourcePrimitive = sourcePrimitives.get(i);
			destPrimitive = destPrimitives.get(i);

			destinationValue = destPrimitive.getValue();
			sourceValue = sourcePrimitive.getValue();

			destPrimitive.setValue(sourceValue);
			sourcePrimitive.setValue(destinationValue);
		}

	}
}