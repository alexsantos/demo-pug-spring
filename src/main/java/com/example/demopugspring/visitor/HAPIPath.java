package com.example.demopugspring.visitor;


public class HAPIPath {

	protected static final String WRONG_PATH = "Wrong path specified.";
	private static final String DEAFULT_INDEX = "1";
	private static final Integer DEFAULT_REP = 0;

	private String structureName;
	private Integer repetition;

	public HAPIPath() {
		structureName = DEAFULT_INDEX;
		repetition = DEFAULT_REP;
	}

	public HAPIPath(String name, Integer repetition) {
		structureName = name;
		this.repetition = repetition;
	}

	public String getStructureName() {
		return structureName;
	}

	public Integer getRepetition() {
		return repetition;
	}

}

