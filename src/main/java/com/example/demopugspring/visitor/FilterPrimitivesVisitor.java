package com.example.demopugspring.visitor;

import java.util.ArrayList;

import com.example.demopugspring.filter.Filter;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Primitive;

/**
 * This class checks if the primitive are according a specific filter, if the
 * filter returns true then hasCondition it's true and the Primitve it's put
 * into the primitivesWithCondition array.
 * 
 * If you want to retrieve all the primitives that are true with the given
 * condition then retrieve the primitivesWithCondition array. If you just want
 * to know at an higher level that if the filter is true for a given primitive
 * you can use the boolean hasCondition.
 *
 */
public class FilterPrimitivesVisitor extends MapperVisitor{

	boolean hasCondition = false;
	ArrayList<Primitive> primitivesWithCondition;
	Filter toFilter;
	
	public FilterPrimitivesVisitor(String path, String value, Filter toFilter) throws HL7Exception {
		super(path, value);
		primitivesWithCondition = new ArrayList<>();
		this.toFilter = toFilter;
	}
	

	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		hasCondition = toFilter.doFilter(type);
		if (hasCondition) {
			primitivesWithCondition.add(type);
		}

		return false;
	}
	
	public boolean primitiveHasCondition() {
		return hasCondition;
	}

	public ArrayList<Primitive> getPrimitivesWithCondition() {
		return primitivesWithCondition;
	}

}
