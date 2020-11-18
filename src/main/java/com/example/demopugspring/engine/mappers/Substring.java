package com.example.demopugspring.engine.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Substring extends AbstractMapper {

	private static final String SUBSTRING_VALUE_REGEX = "-?\\d+,(-?\\d+|\\$)";

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	private static final Pattern SUBSTRING_VALUE_PATTERN = Pattern.compile(SUBSTRING_VALUE_REGEX);

	public Substring(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public List<MapperError> mapKey(String key) {
		ArrayList<MapperError> errors = new ArrayList<MapperError>();

		Matcher valueMatcher = SUBSTRING_VALUE_PATTERN.matcher(value);

		if (valueMatcher.matches()) {
			try {
				String inValue = outgoingTerser.get(key);

				String[] indexes = value.split(",");
				int beginIndex = Integer.parseInt(indexes[0]);
				beginIndex = (beginIndex < 0) ? inValue.length() + beginIndex : beginIndex;
				int endIndex = "$".equals(indexes[1]) ? inValue.length() : Integer.parseInt(indexes[1]);
				endIndex = (endIndex < 0) ? inValue.length() + endIndex : endIndex;

				String outValue = inValue.substring(beginIndex, endIndex);

				outgoingTerser.set(key, outValue);
			} catch (HL7Exception e) {
				log.error(e.getMessage());
				errors.add(new MapperError(key, e.getMessage()));
			}
		} else {
			log.error("Invalid value. Must follow pattern '" + SUBSTRING_VALUE_REGEX + "'.");
			errors.add(new MapperError(key, "Invalid value. Must follow pattern '" + SUBSTRING_VALUE_REGEX + "'."));
		}

		return errors;
	}

}
