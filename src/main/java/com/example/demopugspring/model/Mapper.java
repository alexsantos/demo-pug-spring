package com.example.demopugspring.model;

import java.util.List;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Mapper {

	public enum Category {
			TEXT("Text"),
			FIELD("field"),
			SWAP("swap"),
			TRANSCODING("transcoding"),
			SEGMENT("segment"),
			JOIN("join"),
			NUMERIC("numeric"),
			CONTACT("contact"),
			AFTER_SWAP("after_swap"),
			AFTER_FIELD("after_field"),
			AFTER_JOIN_FIELDS("after_join_fields"),
			CLEAR_IF("clear_if"),
			ADD_SNS("add_sns"),
			REPLACE("replace");

		private final String value;

		Category(String value) {
			this.value = value;
		}

		// @JsonValue
		public String getValue() {
			return value;
		}
	}

	@Id
	@GeneratedValue
	private Long id;
	@ElementCollection(fetch = FetchType.EAGER)
	private List<String> key;
	private String value;
	private Mapper.Category category;

	public Mapper(List<String> key, String value, Mapper.Category category) {
		this.key = key;
		this.value = value;
		this.category = category;
	}

}
