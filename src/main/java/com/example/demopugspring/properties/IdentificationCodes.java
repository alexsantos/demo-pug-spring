package com.example.demopugspring.properties;

import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource(value = "classpath:/properties/IdentificationCodes.properties", encoding = "utf-8")
public class IdentificationCodes extends Codes {

}
