package com.example.demopugspring.operation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import com.example.demopugspring.visitor.ReplaceVisitor;

import java.util.List;

public class ReplaceOperation implements Operation {

	private static String SPLITTER_CHAR = ",";

	private List<String> destinationsPath;
	private String valueToReplace;
	private String regex;

	public ReplaceOperation(String value, List<String> destinations) {
		this.destinationsPath = destinations;
		String[] args = value.split(SPLITTER_CHAR);

		regex = args[0];
		if (args.length > 1) {
			valueToReplace = args[1];
		} else {
			valueToReplace = "";
		}
	}

	@Override
	public void doOperation(Message message) throws HL7Exception {
		for (String destPath : destinationsPath) {
			doOperation(message, destPath);
		}
	}

	@Override
	public void doOperation(Message message, String destination) throws HL7Exception {
		ReplaceVisitor replaceVisitor = new ReplaceVisitor(destination, this.valueToReplace, this.regex);
		replaceVisitor.start(message);
	}

}
