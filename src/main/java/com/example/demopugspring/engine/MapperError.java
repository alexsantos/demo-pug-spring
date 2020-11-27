package com.example.demopugspring.engine;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MapperError implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 299094428263125959L;

	@JsonProperty
	private String field;
	@JsonProperty
	private String error;
}
