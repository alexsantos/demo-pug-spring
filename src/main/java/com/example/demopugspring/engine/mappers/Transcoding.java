package com.example.demopugspring.engine.mappers;

import java.util.List;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;
import com.example.demopugspring.properties.CodesInterface;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public class Transcoding extends AbstractMapper {

	// TODO ADD MAPPER DESCRIPTION
	private static final String MAPPER_DESCRIPTION = "";

	public Transcoding(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {
		super(engine, incomingMessage, outgoingMessage, incomingTerser, outgoingTerser, keys, value);
	}

	@Override
	public String getDescription() {
		return MAPPER_DESCRIPTION;
	}

	@Override
	public void map() {
		log.debug("System:" + value);
		try {

			switch (value) {
			case "ICD-10":
				System.out.println("ICD-10");
				System.out.println(keys);
				outgoingTerser.set(keys.get(0), "1");
				outgoingTerser.set(keys.get(1), "UM");
				outgoingTerser.set(keys.get(2), "ISO");
				break;
			case "GH-LOCATIONS":
				decodeFieldsCodes(keys, engine.getCountryCodes(), incomingTerser, outgoingTerser);
				break;
			case "FACILITIES":
				decodeFieldsCodes(keys, engine.getFacilitiesCodes(), incomingTerser, outgoingTerser);
				break;
			default:
				log.error("No defined code system.");
				errors.add(new MapperError(keys.toString(), "No defined code system: " + value));
			}
		} catch (HL7Exception ex) {
			log.error(ex.getMessage());
			errors.add(new MapperError(keys.toString(), ex.getMessage()));
		}
	}

	@Override
	public void mapKey(String key) {
		throw new UnsupportedOperationException("The method mapKey isn't supported in Transcoding mapper.");
	}

	private void decodeFieldsCodes(List<String> fields, CodesInterface codeInterface, Terser encodedMessage, Terser decodedMessage) throws HL7Exception {
		for (String field : fields) {
			decodedMessage.set(field, codeInterface.getDecodeCode(encodedMessage.get(field)));
		}
	}
}
