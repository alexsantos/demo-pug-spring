package com.example.demopugspring.engine.mappers;

import java.util.List;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Swap extends AbstractMapper {

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	public Swap(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public void mapKey(String key) {
		try {
			outgoingTerser.set(key, incomingTerser.get(value));
			outgoingTerser.set(value, incomingTerser.get(key));
		} catch (HL7Exception e) {
			log.error(e.getMessage());
			errors.add(new MapperError(key, e.getMessage()));
		}
	}
}
