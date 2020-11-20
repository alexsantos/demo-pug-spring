package com.example.demopugspring.visitor;


public class HAPIPath {
	public static String WRONG_PATH = "Wrong path specified.";

	private String structureName;
	private Integer repetition;

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

