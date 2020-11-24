package com.example.demopugspring.visitor;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.StringTokenizer;

import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.Location;
import ca.uhn.hl7v2.model.Composite;
import ca.uhn.hl7v2.model.Field;
import ca.uhn.hl7v2.model.Group;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.MessageVisitor;
import ca.uhn.hl7v2.model.Primitive;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.model.Type;

/**
 * This class is the main class for the visitor implementation, given a path it
 * creates the indexes and it's responsible for the route until reaching the
 * primitive that was suppose to achieve.
 * 
 * While implementing new visitors you can extend this function and override the
 * desired function for example, if you want to change Primitives you can extend
 * this function and then Override the visit Primitive function to get the
 * specific functionality.
 * 
 */

public class StandardVisitor implements MessageVisitor {

	/**
	 * Since composites can be nested, this property is used to keep the state
	 * when visiting them. On a first call to
	 * {@link #start(Composite, Location)} this must be false, so that its Type
	 * at {@link #componentNumber} is visited. But it should turn to true, so
	 * that on a next call to {@link #start(Composite, Location)}, it should
	 * visit the Type at {@link #subComponentNumber} instead.
	 */
	private boolean visitingTopComposite = false;
	private Queue<HAPIPath> groupAndRep;
	private HAPIPath field;
	private HAPIPath component;
	private HAPIPath subComponent;

	private ArrayList<Type> visitedTypes = new ArrayList<>();

	public StandardVisitor(String path) throws HL7Exception {
		groupAndRep = new LinkedList<>();
		getIndexs(path);
	}

	@Override
	public boolean start(Message message) throws HL7Exception {
		HAPIPath element = groupAndRep.poll();

		if (element == null) {
			throw new HL7Exception(HAPIPath.WRONG_PATH);
		}

		int elementRep = element.getRepetition();

		Structure[] structures = message.getAll(element.getStructureName());

		if (elementRep >= 0) {
			structures[elementRep].accept(this, Location.UNKNOWN);
		} else {
			for (Structure structure : structures) {
				structure.accept(this, Location.UNKNOWN);
			}
		}

		return false;
	}

	@Override
	public boolean end(Message message) throws HL7Exception {
		return false;
	}

	@Override
	public boolean start(Group group, Location location) throws HL7Exception {
		HAPIPath element = groupAndRep.poll();

		if (element == null) {
			throw new HL7Exception(HAPIPath.WRONG_PATH);
		}

		int elementRep = element.getRepetition();

		Structure[] structures = group.getAll(element.getStructureName());

		if (elementRep >= 0) {
			structures[elementRep].accept(this, location);
		} else {
			for (Structure structure : structures) {
				structure.accept(this, location);
			}
		}

		return false;
	}

	@Override
	public boolean end(Group group, Location location) throws HL7Exception {
		return false;
	}

	@Override
	public boolean start(Segment segment, Location location) throws HL7Exception {
		String fieldName;
		int fieldNumber;
		if (this.field == null) {
			throw new HL7Exception(HAPIPath.WRONG_PATH);
		}

		fieldName = this.field.getStructureName();
		if (fieldName.equals("#")) {
			fieldNumber = 0;
		} else {
			fieldNumber = Integer.parseInt(fieldName);
		}

		if (fieldNumber > 0) {
			getField(segment, fieldNumber, this.field.getRepetition(), location);
		} else {
			for (int i = 1; i <= segment.numFields(); i++) {
				getField(segment, i, this.field.getRepetition(), location);
			}
		}

		return false;
	}

	@Override
	public boolean end(Segment segment, Location location) throws HL7Exception {
		return false;
	}

	@Override
	public boolean start(Field field, Location location) throws HL7Exception {
		return false;
	}

	@Override
	public boolean end(Field field, Location location) throws HL7Exception {
		return false;
	}

	@Override
	public boolean start(Composite type, Location location) throws HL7Exception {
		Type[] types = type.getComponents();
		HAPIPath hapiPath;
		if (visitingTopComposite) {
			hapiPath = subComponent;
			if (hapiPath == null) {
				throw new HL7Exception(HAPIPath.WRONG_PATH);
			}
			visitComposites(types, hapiPath, location);
		} else {
			visitingTopComposite = true;
			hapiPath = this.component;
			if (hapiPath == null) {
				throw new HL7Exception("Wrong path specified.");
			}

			visitComposites(types, hapiPath, location);
		}
		return false;
	}

	@Override
	public boolean end(Composite type, Location location) throws HL7Exception {
		visitingTopComposite = false;
		return false;
	}

	@Override
	public boolean visit(Primitive type, Location location) throws HL7Exception {
		visitedTypes.add(type);
		return false;
	}

	public List<Type> getVisitedTypes() {
		return visitedTypes;
	}

	/**
	 * Probably this function could be better, but the mission of this function
	 * it's with a given path retrieve each index of the tree, if the position
	 * has a wildcard (#) it will perform all repetitions of that element,
	 * example PID(#)-3-1 it will go for each PID to the position 3-1
	 * 
	 * @param spec
	 *            - the path for a resource
	 * @return
	 * @throws HL7Exception
	 */
	private void getIndexs(String spec) throws HL7Exception {
		StringTokenizer tok = new StringTokenizer(spec, "-", false);
		parseSegmentPathSpec(tok.nextToken());
		field = parsePath(tok);
		component = parsePath(tok);
		subComponent = parsePath(tok);
	}

	/**
	 * Parses the path of the given tokenizer, if the tokenizer has a valid
	 * value it will create the respective HAPIPath otherwise it will create a
	 * default one
	 * 
	 * @param tok
	 * @return
	 * @throws HL7Exception
	 */
	private HAPIPath parsePath(StringTokenizer tok) throws HL7Exception {
		HAPIPath result;
		if (tok.hasMoreTokens()) {
			result = parseNameAndRepetition(tok.nextToken());
		} else {
			result = new HAPIPath();
		}

		return result;
	}

	/**
	 * This method parses if the path given has groups and group reps indicators
	 * If they do they will be parsed and will be added to the structure, for
	 * last it will check and parse the segment and its rep if exists.
	 * 
	 * @param segmentPathSpec
	 * @throws HL7Exception
	 */

	private void parseSegmentPathSpec(String segmentPathSpec) throws HL7Exception {
		String groupToken;
		StringTokenizer segmentPathTokenizer = new StringTokenizer(segmentPathSpec, "/", false);

		while (segmentPathTokenizer.hasMoreTokens()) {
			groupToken = segmentPathTokenizer.nextToken();
			groupAndRep.offer(parseNameAndRepetition(groupToken));
		}

	}

	private HAPIPath parseNameAndRepetition(String nameAndRep) throws HL7Exception {
		String repToken;
		String structureName;
		Integer structureRep = 0;
		StringTokenizer segRepTokenizer = new StringTokenizer(nameAndRep, "()", false);
		structureName = segRepTokenizer.nextToken();
		try {
			if (segRepTokenizer.hasMoreTokens()) {
				repToken = segRepTokenizer.nextToken();
				structureRep = (repToken.equals("#")) ? -1 : Integer.parseInt(repToken);
			}
		} catch (NumberFormatException e) {
			throw new HL7Exception("Invalid integer next to  " + structureName);
		}
		return new HAPIPath(structureName, structureRep);
	}

	private void getField(Segment segment, int fieldNumber, int fieldRepetition, Location location) throws HL7Exception {
		Type[] typeFields;

		typeFields = segment.getField(fieldNumber);
		if (fieldRepetition >= 0) {
			accessField(typeFields[fieldRepetition], location);
		} else {
			for (Type typeField : typeFields) {
				accessField(typeField, location);
			}
		}
	}

	private void accessField(Type type, Location location) throws HL7Exception {
		if (type.isEmpty() && type instanceof Composite) {
			((Composite) type).accept(this, location);
		} else {
			type.accept(this, location);
		}
	}

	private void visitComposites(Type[] types, HAPIPath hapiPath, Location location) throws HL7Exception {
		int primitivetNumber = 0;
		String compositeIndex = hapiPath.getStructureName();

		if (!compositeIndex.equals("#")) {
			primitivetNumber = Integer.parseInt(compositeIndex);
		}

		if (primitivetNumber > 0) {
			types[primitivetNumber - 1].accept(this, location);
		} else {
			for (Type componentType : types) {
				componentType.accept(this, location);
			}
		}
	}

}
