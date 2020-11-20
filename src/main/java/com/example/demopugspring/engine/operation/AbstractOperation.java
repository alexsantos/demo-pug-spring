package com.example.demopugspring.engine.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demopugspring.controller.IntegrationRestController;
import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

public abstract class AbstractOperation {

	protected static final Logger log = LoggerFactory.getLogger(IntegrationRestController.class);

	protected MapperEngine engine;

	protected Message incomingMessage;
	protected Message outgoingMessage;

	protected Terser incomingTerser;
	protected Terser outgoingTerser;

	protected List<String> keys;
	protected String value;

	protected ArrayList<MapperError> errors = new ArrayList<MapperError>();

	public List<MapperError> getErrors() {
		return errors;
	}

	public AbstractOperation(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {

		this.engine = engine;

		this.incomingMessage = incomingMessage;
		this.outgoingMessage = outgoingMessage;

		this.incomingTerser = incomingTerser;
		this.outgoingTerser = outgoingTerser;

		this.keys = keys;
		this.value = value;
	}

	public void map() {
		for (String key : keys) {
			mapKey(key);
		}
	}

	public abstract void mapKey(String key);

	@Override
	public int hashCode() {
		return Objects.hash(incomingMessage, incomingTerser, keys, outgoingMessage, outgoingTerser, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof AbstractOperation)) {
			return false;
		}
		AbstractOperation other = (AbstractOperation) obj;
		return Objects.equals(incomingMessage, other.incomingMessage) && Objects.equals(incomingTerser, other.incomingTerser) && Objects.equals(keys, other.keys) && Objects.equals(outgoingMessage, other.outgoingMessage) && Objects.equals(outgoingTerser, other.outgoingTerser) && Objects.equals(value, other.value);
	}
}
