package com.example.demopugspring.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class Response {
    List<MapperError> errorList;
    String message;
}
