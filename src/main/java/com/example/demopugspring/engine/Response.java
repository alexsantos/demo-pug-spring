package com.example.demopugspring.engine;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Response implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4497627874481741772L;

	@JsonProperty
	private List<MapperError> errorList;
	@JsonProperty
	private String message;
}
