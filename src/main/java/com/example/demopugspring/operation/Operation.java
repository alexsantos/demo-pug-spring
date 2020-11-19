package com.example.demopugspring.operation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;

public interface Operation {
	public void doOperation(Message message) throws HL7Exception;

	public void doOperation(Message message, String destination) throws HL7Exception;
}
