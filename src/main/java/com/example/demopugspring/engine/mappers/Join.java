package com.example.demopugspring.engine.mappers;

import java.util.ArrayList;
import java.util.List;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Join extends AbstractMapper {

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	public Join(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public List<MapperError> mapKey(String key) {
		ArrayList<MapperError> errors = new ArrayList<MapperError>();

		StringBuilder joined = new StringBuilder();
		log.debug("Fields to join:" + value);

		for (String val : value.split(",")) {
			try {
				String fieldVal = incomingTerser.get(val);
				log.debug("Value for " + val + ":" + fieldVal);
				joined.append(fieldVal);
			} catch (HL7Exception e) {
				log.error(e.getMessage());
				errors.add(new MapperError(key, e.getMessage()));
			}
		}
		log.debug("joined fields:" + joined.toString());

		try {
			outgoingTerser.set(key, joined.toString());
		} catch (HL7Exception e) {
			log.error(e.getMessage());
			errors.add(new MapperError(key, e.getMessage()));
		}

		return errors;
	}
}
