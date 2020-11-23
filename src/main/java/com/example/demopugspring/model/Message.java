package com.example.demopugspring.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@Table(uniqueConstraints = {@UniqueConstraint(name = "uk_code_event_version", columnNames = {"code", "event", "version"})})
public class Message {

	@Id
	@GeneratedValue
	private Long id;
	private String code;
	private String event;
	private Version version;
	public Message(String code, String event, Version version) {
		this.code = code;
		this.event = event;
		this.version = version;
	}


	public enum Version {
		v24("2.4"),
		v25("2.5"),
		v251("2.5.1");

		private final String value;

		Version(String value) {
			this.value = value;
		}

		public static Version fromString(String text) {
			for (Version v : Version.values()) {
				if (v.value.equalsIgnoreCase(text)) {
					return v;
				}
			}
			return null;
		}

		//@JsonValue
		public String getValue() {
			return value;
		}
	}
}
