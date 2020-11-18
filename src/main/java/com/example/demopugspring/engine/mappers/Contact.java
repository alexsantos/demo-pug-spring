package com.example.demopugspring.engine.mappers;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Contact extends AbstractMapper {

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	public Contact(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public List<MapperError> mapKey(String key) {
		ArrayList<MapperError> errors = new ArrayList<MapperError>();

		try {
			addRepetitions(outgoingTerser, outgoingTerser.get("PID-13-12-1"), outgoingTerser.get("PID-14-7-1"), outgoingTerser.get("PID-13-4"));
		} catch (HL7Exception e) {
			log.error(e.getMessage());
			errors.add(new MapperError(key, e.getMessage()));
		}

		return errors;
	}

	private void addRepetitions(Terser tmp, String... strings) throws HL7Exception {
		StringBuffer listContactsHome = new StringBuffer();
		int i = 0;
		for (String s : strings) {
			if (StringUtils.isEmpty(s)) {
				continue;
			}
			boolean isPhone = s.matches("[\\d]+");
			if (isPhone) {
				tmp.set("PID-13(" + i + ")-3", "PH");
				tmp.set("PID-13(" + i + ")-12", s);
				tmp.set("PID-13(" + i + ")-4", "");
			} else {
				tmp.set("PID-13(" + i + ")-3", "X.400");
				tmp.set("PID-13(" + i + ")-4", s);
				tmp.set("PID-13(" + i + ")-12", "");
			}
			tmp.set("PID-14(" + i + ")-7", "");

			i++;
		}
	}

}
