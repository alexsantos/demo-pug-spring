package com.example.demopugspring.properties;

public enum PropertiesCategoriesEnum {
	FACILITIES("FACILITIES"),
	GH_LOCATIONS("GH-LOCATIONS"),
	IDENTIFICATIONS("IDENTIFICATIONS"),
	MARRIAGE_STATUS("MARRIAGE-STATUS"),
	INSURERS_CODES("INSURERS-CODES"),
	ICD_10("ICD-10"),
	UNKNOWN("UNKNOUN");

	private final String value;

	PropertiesCategoriesEnum(String value) {
		this.value = value;
	}

	public static PropertiesCategoriesEnum valueOfProperty(String value) {
		PropertiesCategoriesEnum result = UNKNOWN;
		for (PropertiesCategoriesEnum property : PropertiesCategoriesEnum.values()) {
			if (value.equals(property.getValue())) {
				result = property;
				break;
			}
		}
		return result;
	}

	public String getValue() {
		return this.value;
	}

}
