package com.example.demopugspring.engine.operation;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.demopugspring.engine.MapperEngine;
import com.example.demopugspring.engine.MapperError;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.util.Terser;

/**
 * This abstract class serves as the base for any Operation to be applied to an
 * HL7 message. In order to implement this class, one would only need to
 * implement method {@link #map()}.
 * </p>
 * An example of implementing this class can be seen at {@link Text}.
 * </p>
 * There are Operations (such as {@link Field}) that may need to grab values
 * either from the original message (MSG) or the current one (TMP). For this
 * reason, {@link #value} may be prefixed by {@code "/[MSG]"} or
 * {@code "/[TMP]"}.
 * 
 * @author Luis Torres Costa (luis.torres.costa@jmellosaude.pt)
 *
 */
public abstract class AbstractOperation {

	private static final String USE_MSG_VALUE_PREFIX = "/[MSG]";
	private static final String USE_TMP_VALUE_PREFIX = "/[TMP]";

	protected static final Logger log = LoggerFactory.getLogger(AbstractOperation.class);

	protected MapperEngine engine;

	protected Message incomingMessage;
	protected Message outgoingMessage;

	protected Terser incomingTerser;
	protected Terser outgoingTerser;

	protected List<String> keys;
	protected String value;

	private boolean useOriginalValue;

	protected ArrayList<MapperError> errors = new ArrayList<>();

	public boolean getUseOriginalValue() {
		return useOriginalValue;
	}

	public AbstractOperation(MapperEngine engine, Message incomingMessage, Message outgoingMessage, Terser incomingTerser, Terser outgoingTerser, List<String> keys, String value) {

		this.engine = engine;

		this.incomingMessage = incomingMessage;
		this.outgoingMessage = outgoingMessage;

		this.incomingTerser = incomingTerser;
		this.outgoingTerser = outgoingTerser;

		this.keys = keys;
		this.useOriginalValue = false;

		if (value.startsWith(USE_MSG_VALUE_PREFIX)) {
			this.value = value.substring(USE_MSG_VALUE_PREFIX.length());
			this.useOriginalValue = true;
		} else if (value.startsWith(USE_TMP_VALUE_PREFIX)) {
			this.value = value.substring(USE_TMP_VALUE_PREFIX.length());
		} else {
			this.value = value;
		}
	}

	public List<MapperError> getErrors() {
		return errors;
	}

	public abstract void map();

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
