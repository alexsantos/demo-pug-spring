package com.example.demopugspring.engine.mappers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Date extends AbstractMapper {

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");

	public Date(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public void mapKey(String key) {
		String inDate = null;
		try {
			inDate = outgoingTerser.get(key);
			java.util.Date outDate = new SimpleDateFormat(value).parse(inDate);
			outgoingTerser.set(key, DATE_FORMAT.format(outDate));
		} catch (HL7Exception e) {
			log.error(e.getMessage());
			errors.add(new MapperError(key, e.getMessage()));
		} catch (ParseException e) {
			log.error("Couldn't parse date '" + inDate + "' with format '" + value + "'.", e.getMessage());
			errors.add(new MapperError(key, "Couldn't parse date '" + inDate + "' with format '" + value + "'."));
		}
	}
}
