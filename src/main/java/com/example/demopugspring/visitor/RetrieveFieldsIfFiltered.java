package com.example.demopugspring.visitor;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Composite;
import com.example.demopugspring.filter.Filter;

import java.util.ArrayList;

public class RetrieveFieldsIfFiltered extends FilterPrimitivesVisitor {

	ArrayList<Composite> composesFiltered;

	public RetrieveFieldsIfFiltered(String path, String value, Filter filter) throws HL7Exception {
		super(path, value, filter);
		composesFiltered = new ArrayList<>();
	}

	@Override
	public boolean end(Composite type, Location location) throws HL7Exception {
		noSubComponent();
		if (primitiveHasCondition()) {
			composesFiltered.add(type);
		}

		return false;
	}

	public ArrayList<Composite> getComposesFiltered() {
		return composesFiltered;
	}

}
