package com.example.demopugspring.visitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Primitive;

import java.util.ArrayList;

public class RetrievePrimitivesVisitor extends MapperVisitor {

	ArrayList<Primitive> primitives;

	public RetrievePrimitivesVisitor(String path, String value) throws HL7Exception {
		super(path, value);
		primitives = new ArrayList<>();

	}

	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		primitives.add(type);
		return false;
	}

	public ArrayList<Primitive> getPrimitives() {
		return primitives;
	}

}
