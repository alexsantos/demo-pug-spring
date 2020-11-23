package com.example.demopugspring.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MapperError {
	private String field;
	private String error;
}
