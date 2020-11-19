package com.example.demopugspring.operation;

import java.util.List;

import com.example.demopugspring.filter.Filter;
import com.example.demopugspring.visitor.RetrieveFieldsIfFiltered;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Message;

public class ClearFilteredOperation implements Operation{

	String value;
	Filter filter;
	List<String> destinationsPath;
	
	
	public ClearFilteredOperation(String value, List<String> destinations, Filter filter) {
		this.value = value;
		this.filter = filter;
		this.destinationsPath = destinations;
	}
	
	@Override
	public void doOperation(Message message) throws HL7Exception {
		for(String destination : destinationsPath) {
			doOperation(message, destination);
		}
	}
	
	@Override
	public void doOperation(Message message, String destination) throws HL7Exception {
		RetrieveFieldsIfFiltered retriever = new RetrieveFieldsIfFiltered(destination, value, filter);
		retriever.start(message);
		for(Composite composite : retriever.getComposesFiltered()) {
			composite.clear();
		}
	}

}
