package com.example.demopugspring.engine.operation;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Type;
import ca.uhn.hl7v2.util.Terser;
import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;
import com.example.demopugspring.visitor.StandardVisitor;

import java.util.List;

public class Text extends AbstractOperation {

	public Text(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public void mapKey(String key) {
		try {
			if (key.contains("#")) {
				StandardVisitor visitor = new StandardVisitor(key);
				visitor.start(outgoingMessage);
				for (Type type : visitor.getVisitedTypes()) {
					type.parse(value);
				}
			} else {
				outgoingTerser.set(key, value);
			}
		} catch (HL7Exception e) {
			log.error(e.getMessage());
			errors.add(new MapperError(key, e.getMessage()));
		}
	}

}
